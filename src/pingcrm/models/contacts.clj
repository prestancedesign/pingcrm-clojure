(ns pingcrm.models.contacts
  (:require [honey.sql :as h]
            [honey.sql.helpers :as helpers]
            [next.jdbc :as jdbc]))

(defn count-contacts [db]
  (let [query (h/format {:select [[:%count.* :aggregate]]
                         :from [:contacts]
                         :where [:and
                                 [:= :account_id 1]
                                 [:<> :account_id nil]
                                 [:= :deleted_at nil]]})]
    (jdbc/execute-one! db query)))

(defn retrieve-and-filter-contacts
  [db filters offset]
  ;;TODO: Add filter by organization name
  (let [search-filter (when-let [search (:search filters)]
                        [:or [:like :c.first_name (str "%" search "%")]
                         [:like :c.last_name (str "%" search "%")]
                         [:like :c.email (str "%" search "%")]])
        trashed-filter (case (:trashed filters)
                         "with" nil
                         "only" [:<> :c.deleted_at nil]
                         [:= :c.deleted_at nil])
        all-filters (helpers/where search-filter trashed-filter)
        query (h/format (merge {:select [[:c.*] [[:|| :c.first_name " " :c.last_name] :name] [:o.name :organization]]
                                :from [[:contacts :c]]
                                :left-join [[:organizations :o] [:= :c.organization_id :o.id]]
                                :order-by [:last_name :first_name]
                                :limit 10
                                :offset offset}
                               all-filters))]
    (jdbc/execute! db query)))
