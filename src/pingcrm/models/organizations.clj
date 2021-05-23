(ns pingcrm.models.organizations
  (:require [honey.sql :as h]
            [honey.sql.helpers :as helpers]
            [next.jdbc :as jdbc]))

;; select count(*) as aggregate from "organizations" where "organizations"."account_id" = 1 and "organizations"."account_id" is not null and "organizations"."deleted_at" is null

(defn count-organizations [db]
  (let [query (h/format {:select [[:%count.* :aggregate]]
                         :from [:organizations]
                         :where [:and
                                 [:= :account_id 1]
                                 [:<> :account_id nil]
                                 [:= :deleted_at nil]]})]
    (jdbc/execute-one! db query)))

;; select * from "organizations" where "organizations"."account_id" = 1 and "organizations"."account_id" is not null and "organizations"."deleted_at" is null order by "name" asc limit 10 offset 0

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
