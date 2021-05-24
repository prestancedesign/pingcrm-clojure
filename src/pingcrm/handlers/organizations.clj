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

(defn delete-organization!
  [db]
  (fn [req]
    (let [id (-> req :path-params :organization-id)
          back (get (:headers req) "referer")
          organization-deleted? (org-db/soft-delete-organization! db id)]
      (when organization-deleted?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "Organization deleted."}))))))

(defn restore-organization!
  [db]
  (fn [req]
    (let [id (-> req :path-params :organization-id)
          back (get (:headers req) "referer")
          organization-restored? (org-db/restore-deleted-organization! db id)]
      (when organization-restored?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "Organization restored."}))))))