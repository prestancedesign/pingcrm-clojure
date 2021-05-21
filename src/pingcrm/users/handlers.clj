(ns pingcrm.users.handlers
  (:require [crypto.password.bcrypt :as password]
            [inertia.middleware :as inertia]
            [pingcrm.db :as db]
            [ring.util.response :as rr]))

(defn get-users [{:keys [params]}]
  (let [filters (select-keys params [:search :role :trashed])
        props {:users (db/retrieve-and-filter-users filters)
               :filters filters}]
    (inertia/render "Users/Index" props)))

(defn create-user!
  [req]
  (let [account-id (-> req :identity :account_id)
        user (-> req :body-params)
        encrypted-user (update user :password password/encrypt)
        user-created? (db/insert-user! (assoc encrypted-user :account_id account-id))]
    (when user-created?
      (-> (rr/redirect "/users")
          (assoc :flash {:success "User created."})))))

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

(defn delete-user! [req]
  (let [id (-> req :path-params :user-id)
        back (get (:headers req) "referer")
        user-deleted? (db/soft-delete-user! id)]
    (when user-deleted?
      (-> (rr/redirect back :see-other)
          (assoc :flash {:success "User deleted."})))))

(defn restore-user! [req]
  (let [id (-> req :path-params :user-id)
        back (get (:headers req) "referer")
        user-restored? (db/restore-deleted-user! id)]
    (when user-restored?
      (-> (rr/redirect back :see-other)
          (assoc :flash {:success "User restored."})))))
