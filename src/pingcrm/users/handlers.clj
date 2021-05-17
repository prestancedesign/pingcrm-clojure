(ns pingcrm.users.handlers
  (:require [inertia.middleware :as inertia]
            [pingcrm.db :as db]
            [ring.util.response :as rr]))

(defn get-users [{:keys [params]}]
  (let [filters (select-keys params [:search :role :trashed])
        props {:users (db/retrieve-and-filter-users filters)
               :filters filters}]
    (inertia/render "Users/Index" props)))

(defn edit-user! [{:keys [path-params]}]
  (let [props {:user (db/get-user-by-id (:user-id path-params))}]
    (inertia/render "Users/Edit" props)))

(defn update-user! [req]
  (let [id (-> req :path-params :user-id)
        url (str (-> req :uri) "/edit")
        user-form (select-keys (:body-params req) [:first_name :last_name :email :owner])
        user-updated? (db/update-user! user-form id)]
    (when user-updated?
      (-> (rr/redirect url :see-other)
          (assoc :flash {:success "User updated."})))))
