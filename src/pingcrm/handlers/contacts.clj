(ns pingcrm.handlers.contacts
  (:require [inertia.middleware :as inertia]
            [pingcrm.models.contacts :as db]
            [pingcrm.shared.pagination :as pagination]
            [ring.util.response :as rr]))

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
