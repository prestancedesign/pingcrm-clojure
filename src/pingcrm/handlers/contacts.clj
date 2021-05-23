(ns pingcrm.handlers.contacts
  (:require [inertia.middleware :as inertia]
            [pingcrm.shared.pagination :as pagination]
            [pingcrm.models.contacts :as org-db]))

(defn index
  [db]
  (fn [{:keys [params uri] :as request}]
    (let [filters (select-keys params [:search :trashed])
          page (get-in request [:parameters :query :page] 1)
          offset (* (dec page) 10)
          count (:aggregate (org-db/count-contacts db))
          contacts (org-db/retrieve-and-filter-contacts db filters offset)
          contacts-formated (map #(assoc % :organization {:name (:organization %)}) contacts)
          props {:contacts {:data contacts-formated
                            :current_page page
                            :links (pagination/pagination-links uri page count 10)}
                 :filters filters}]
      (inertia/render "Contacts/Index" props))))
