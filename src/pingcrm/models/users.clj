(ns pingcrm.models.users
  (:require [honey.sql :as h]
            [honey.sql.helpers :refer [where]]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]
            [clojure.set :as set]))

(extend-protocol rs/ReadableColumn
  Integer
  (read-column-by-index [x mrs i]
    (if (= (.getColumnName mrs i) "owner")
      (not (zero? x))
      x)))

(defn retrieve-and-filter-users
  [db {:keys [search role trashed]}]
  (let [query (h/format
               (cond-> {:select [:id [[:|| :first_name " " :last_name] :name] :email :owner :deleted_at]
                        :from [:users]
                        :order-by [:last_name :first_name]}
                 search (where [:or [:like :first_name (str "%" search "%")]
                                [:like :last_name (str "%" search "%")]
                                [:like :email (str "%" search "%")]])
                 role (where (case role
                               "owner" [:= :owner true]
                               "user" [:= :owner false]))
                 true (where (case trashed
                               "with" nil
                               "only" [:<> :deleted_at nil]
                               [:= :deleted_at nil]))))]
    (jdbc/execute! db query)))

(defn get-user-by-id
  [db id]
  ;;TODO: Use with-open for better performance
  (let [user (sql/get-by-id db :users id)
        account-id (:account_id user)
        account (sql/get-by-id db :accounts account-id {:columns [:id :name]})]
    (assoc user :account account)))

(defn get-user-by-email
  [db email]
  (sql/get-by-id db :users email :email {}))

(defn insert-user!
  [db user]
  (let [user (set/rename-keys user {:photo :photo_path})
        query (h/format{:insert-into :users
                             :values [(merge user {:created_at :current_timestamp
                                                   :updated_at :current_timestamp})]})]
    (jdbc/execute-one! db query)))

(defn update-user!
  [db user id]
  (sql/update! db :users user {:id id}))

(defn soft-delete-user!
  [db id]
  (let [query (h/format {:update :users
                         :set {:deleted_at :current_timestamp
                               :updated_at :current_timestamp}
                         :where [:= :id id]})]
    (jdbc/execute-one! db query)))

(defn restore-deleted-user!
  [db id]
  (let [query (h/format {:update :users
                         :set {:deleted_at nil
                               :updated_at :current_timestamp}
                         :where [:= :id id]})]
    (jdbc/execute-one! db query)))
