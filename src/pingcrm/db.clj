(ns pingcrm.db
  (:require [honey.sql :as h]
            [honey.sql.helpers :as helpers]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]))

(def ds (jdbc/get-datasource {:dbtype "sqlite" :dbname "database/database.sqlite"}))

(def db (jdbc/with-options ds {:builder-fn rs/as-unqualified-maps}))

(extend-protocol rs/ReadableColumn
  Integer
  (read-column-by-index [x mrs i]
    (if (= (.getColumnName mrs i) "owner")
      (not (zero? x))
      x)))

(defn search-filter-regex [column-name pattern]
  [:like column-name (str "%" pattern "%")])

(defn retrieve-and-filter-users
  [filters]
  (let [search-filter (when-let [search (:search filters)]
                        [:or (search-filter-regex :first_name search)
                         (search-filter-regex :last_name search)
                         (search-filter-regex :email search)])
        role-filter (when-let [role (:role filters)]
                      (case role
                        "owner" [:= :owner true]
                        "user" [:= :owner false]))
        trashed-filter (case (:trashed filters)
                         "with" nil
                         "only" [:<> :deleted_at nil]
                         [:= :deleted_at nil])
        all-filters (helpers/where search-filter role-filter trashed-filter)
        query (h/format (merge {:select [:id [[:|| :first_name " " :last_name] :name] :email :owner :deleted_at]
                                :from [:users]
                                :order-by [:last_name :first_name]}
                               all-filters))]
    (jdbc/execute! db query)))

(defn get-user-by-id
  [id]
  ;;TODO: Use with-open for better performance
  (let [user (sql/get-by-id db :users id)
        account-id (:account_id user)
        account (sql/get-by-id db :accounts account-id {:columns [:id :name]})]
    (assoc user :account account)))

(defn get-user-by-email
  [email]
  (sql/get-by-id db :users email :email {}))

(defn update-user!
  [user id]
  (sql/update! db :users user {:id id}))

(defn soft-delete-user!
  [id]
  (sql/update! db :users {:deleted_at (java.util.Date.)} {:id id}))

(defn restore-deleted-user!
  [id]
  (sql/update! db :users {:deleted_at nil} {:id id}))
