(ns pingcrm.handlers.contacts
  (:require [inertia.middleware :as inertia]
            [pingcrm.models.contacts :as db]
            [pingcrm.shared.pagination :as pagination]
            [ring.util.response :as rr]
            [pingcrm.models.organizations :as org-db]))

(defn index
  [db]
  (fn [{:keys [params uri] :as request}]
    (let [filters (select-keys params [:search :trashed])
          page (get-in request [:parameters :query :page] 1)
          offset (* (dec page) 10)
          count (:aggregate (db/count-contacts db))
          contacts (db/retrieve-and-filter-contacts db filters offset)
          contacts-formated (map #(assoc % :organization {:name (:organization %)}) contacts)
          props {:contacts {:data contacts-formated
                            :current_page page
                            :links (pagination/pagination-links uri page count 10)}
                 :filters filters}]
      (inertia/render "Contacts/Index" props))))

(defn contacts-form [_]
  (inertia/render "Contacts/Create"))

(defn store-contact! [db]
  (fn [{:keys [body-params] :as req}]
    (let [account-id (-> req :identity :account_id)
          contacts-created? (db/insert-contact! db (assoc body-params :account_id account-id))]
      (when contacts-created?
        (-> (rr/redirect "/contacts")
            (assoc :flash {:success "Contacts created."}))))))

(defn edit-contact! [db]
  (fn [{:keys [path-params]}]
    (let [props {:contact (db/get-contact-by-id db (:contact-id path-params))
                 :organizations (org-db/list-organizations db)}]
      (inertia/render "Contacts/Edit" props))))

(defn update-contact! [db]
  (fn [{:keys [body-params] :as req}]
    (let [id (-> req :path-params :contact-id)
          url (str (-> req :uri) "/edit")
          ;; contact-form (select-keys (:body-params req) [:first_name :last_name :email :owner])
          contact-updated? (db/update-contact! db body-params id)]
      (when contact-updated?
        (-> (rr/redirect url :see-other)
            (assoc :flash {:success "Contact updated."}))))))

(defn delete-contact! [db]
  (fn [req]
    (let [id (-> req :path-params :contact-id)
          back (get (:headers req) "referer")
          contact-deleted? (db/soft-delete-contact! db id)]
      (when contact-deleted?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "Contact deleted."}))))))

(defn restore-contact! [db]
  (fn [req]
    (let [id (-> req :path-params :contact-id)
          back (get (:headers req) "referer")
          contact-restored? (db/restore-deleted-contact! db id)]
      (when contact-restored?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "Contact restored."}))))))
