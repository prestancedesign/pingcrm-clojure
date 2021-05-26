(ns pingcrm.handlers.auth
  (:require [crypto.password.bcrypt :as password]
            [inertia.middleware :as inertia]
            [pingcrm.models.users :as db]
            [ring.util.response :as rr]))

(defn login [_]
  (inertia/render "Auth/Login"))

(defn login-authenticate
  "Check request username and password against authdata
  username and passwords."
  [db]
  (fn [request]
    (let [email (-> request :body-params :email)
          password (-> request :body-params :password)
          user (db/get-user-by-email db email)
          sanitized-user (dissoc user :password)
          session (:session request)]
      (if (and user (password/check password (:password user)))
        (let [updated-session (assoc session :identity sanitized-user)]
          (-> (rr/redirect "/")
              (assoc :session updated-session)))
        (-> (rr/redirect "/login")
            (assoc :flash {:error {:email "These credentials do not match our records."}}))))))

(defn logout [_]
  (-> (rr/redirect "/" :see-other)
      (assoc :session nil)))
