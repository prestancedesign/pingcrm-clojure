(ns pingcrm.handlers.organizations
  (:require [inertia.middleware :as inertia]
            [pingcrm.shared.pagination :as pagination]
            [pingcrm.models.organizations :as org-db]))

(defn index
  [db]
  (fn [{:keys [params] :as request}]
    (let [filters (select-keys params [:search :trashed])
          page (get-in request [:parameters :query :page] 1)
          offset (* (dec page) 10)
          count (:aggregate (org-db/count-organizations db))
          organizations (org-db/retrieve-and-filter-organizations db filters offset)
          props {:organizations {:data organizations
                                 :current_page page
                                 :links (pagination/pagination-links page count 10)}
                 :filters filters}]
      (inertia/render "Organizations/Index" props))))
