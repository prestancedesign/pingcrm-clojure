(ns pingcrm.handlers.users
  (:require [crypto.password.bcrypt :as password]
            [inertia.middleware :as inertia]
            [pingcrm.models.users :as db]
            [ring.util.response :as rr]
            [struct.core :as st]))

(def user-validation
  [[:first_name st/required st/string]
   [:last_name st/required st/string]
   [:email st/required st/email]
   [:owner st/required]])

(defn validate-user [params]
  (first (st/validate params user-validation)))

(defn validate-unique-user
  [db params]
  (let [{:keys [email]} params
        validation (validate-user params)]
    (if (db/get-user-by-email  db email)
      (assoc validation :email "The email has already been taken.")
      validation)))

(defn get-users [db]
  (fn [{:keys [params]}]
    (let [filters (select-keys params [:search :role :trashed])
          props {:users (db/retrieve-and-filter-users db filters)
                 :filters filters}]
      (inertia/render "Users/Index" props))))

(defn user-form [_]
  (inertia/render "Users/Create"))

(defn store-user! [db]
  (fn [{:keys [body-params] :as req}]
    (if-let [errors (validate-unique-user db body-params)]
      (-> (rr/redirect "/users/create")
          (assoc :flash {:error errors}))
      (let [account-id (-> req :identity :account_id)
            user body-params
            encrypted-user (update user :password password/encrypt)
            user-created? (db/insert-user! db (assoc encrypted-user :account_id account-id))]
        (when user-created?
          (-> (rr/redirect "/users")
              (assoc :flash {:success "User created."})))))))

(defn edit-user! [db]
  (fn [{:keys [path-params]}]
    (let [props {:user (db/get-user-by-id db (:user-id path-params))}]
      (inertia/render "Users/Edit" props))))

(defn update-user! [db]
  (fn [{:keys [body-params] :as req}]
    (let [id (-> req :path-params :user-id)
          url (str (-> req :uri) "/edit")]
      (if-let [errors (validate-user body-params)]
        (-> (rr/redirect url :see-other)
            (assoc :flash {:error errors}))
        (let [user-form (select-keys (:body-params req) [:first_name :last_name :email :owner])
              user-updated? (db/update-user! db user-form id)]
          (when user-updated?
            (-> (rr/redirect url :see-other)
                (assoc :flash {:success "User updated."}))))))))

(defn delete-user! [db]
  (fn [req]
    (let [id (-> req :path-params :user-id)
          back (get (:headers req) "referer")
          user-deleted? (db/soft-delete-user! db id)]
      (when user-deleted?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "User deleted."}))))))

(defn restore-user! [db]
  (fn [req]
    (let [id (-> req :path-params :user-id)
          back (get (:headers req) "referer")
          user-restored? (db/restore-deleted-user! db id)]
      (when user-restored?
        (-> (rr/redirect back :see-other)
            (assoc :flash {:success "User restored."}))))))
