(ns pingcrm.handlers.organizations
  (:require [inertia.middleware :as inertia]
            [pingcrm.models.organizations :as org-db]))

(defn index
  [db]
  (fn [{:keys [params]}]
    (let [filters (select-keys params [:search :trashed])
          props {:organizations {:data (org-db/retrieve-and-filter-organization db filters)}
                 :filters filters}]
      (inertia/render "Organizations/Index" props))))
