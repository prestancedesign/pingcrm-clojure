(ns pingcrm.models.organizations
  (:require [honey.sql :as h]
            [honey.sql.helpers :as helpers]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))

(defn count-organizations [db]
  (let [query (h/format {:select [[:%count.* :aggregate]]
                         :from [:organizations]
                         :where [:and
                                 [:= :account_id 1]
                                 [:<> :account_id nil]
                                 [:= :deleted_at nil]]})]
    (jdbc/execute-one! db query)))

(defn retrieve-and-filter-organizations
  [db filters offset]
  (let [search-filter (when-let [search (:search filters)]
                        [:like :name (str "%" search "%")])
        trashed-filter (case (:trashed filters)
                         "with" nil
                         "only" [:<> :deleted_at nil]
                         [:= :deleted_at nil])
        all-filters (helpers/where search-filter trashed-filter)
        query (h/format (merge {:select [:*]
                                :from [:organizations]
                                :order-by [:name]
                                :limit 10
                                :offset offset}
                               all-filters))]
    (jdbc/execute! db query)))

(defn get-organization-by-id
  [db id]
  ;;TODO: Use with-open for better performance
  (let [organization (sql/get-by-id db :organizations id)
        contacts (sql/find-by-keys db :contacts {:organization_id id} {:columns [:id ["first_name || \" \" || last_name" :name] :city :phone]})]
    (assoc organization :contacts contacts)))

(defn update-organization!
  [db organization id]
  (sql/update! db :organizations organization {:id id}))

(defn soft-delete-organization!
  [db id]
  (let [query (h/format {:update :organizations
                         :set {:deleted_at :current_timestamp
                               :updated_at :current_timestamp}
                         :where [:= :id id]})]
    (jdbc/execute-one! db query)))

(defn restore-deleted-organization!
  [db id]
  (let [query (h/format {:update :organizations
                         :set {:deleted_at nil
                               :updated_at :current_timestamp}
                         :where [:= :id id]})]
    (jdbc/execute-one! db query)))
