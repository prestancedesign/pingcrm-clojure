(ns pingcrm.handlers.organizations
  (:require [inertia.middleware :as inertia]
            [pingcrm.models.organizations :as org-db]
            [pingcrm.shared.pagination :as pagination]
            [ring.util.response :as rr]))

(defn index
  [db]
  (fn [{:keys [params uri] :as request}]
    (let [filters (select-keys params [:search :trashed])
          page (get-in request [:parameters :query :page] 1)
          offset (* (dec page) 10)
          count (:aggregate (org-db/count-organizations db))
          organizations (org-db/retrieve-and-filter-organizations db filters offset)
          props {:organizations {:data organizations
                                 :current_page page
                                 :links (pagination/pagination-links uri page count 10)}
                 :filters filters}]
      (inertia/render "Organizations/Index" props))))

(defn edit-organization!
  [db]
  (fn [{:keys [path-params]}]
    (let [props {:organization (org-db/get-organization-by-id db (:organization-id path-params))}]
      (inertia/render "Organizations/Edit" props))))

(defn update-organization!
  [db]
  (fn [{:keys [body-params] :as req}]
    (let [id (-> req :path-params :organization-id)
          url (str (-> req :uri) "/edit")
          organization-updated? (org-db/update-organization! db body-params id)]
      (when organization-updated?
        (-> (rr/redirect url :see-other)
            (assoc :flash {:success "Organization updated."}))))))
