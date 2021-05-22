(ns pingcrm.models.organizations
  (:require [honey.sql :as h]
            [honey.sql.helpers :as helpers]
            [next.jdbc :as jdbc]))

;; select count(*) as aggregate from "organizations" where "organizations"."account_id" = 1 and "organizations"."account_id" is not null and "organizations"."deleted_at" is null

;; select * from "organizations" where "organizations"."account_id" = 1 and "organizations"."account_id" is not null and "organizations"."deleted_at" is null order by "name" asc limit 10 offset 0

(defn retrieve-and-filter-organization
  [db filters]
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
                                :limit 10}
                               all-filters))]
    (jdbc/execute! db query)))
