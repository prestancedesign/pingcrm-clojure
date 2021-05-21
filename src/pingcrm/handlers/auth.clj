(ns pingcrm.handlers.auth
  (:require [crypto.password.bcrypt :as password]
            [pingcrm.models.users :as db]
            [ring.util.response :as rr]))

(defn login-authenticate
  "Check request username and password against authdata
  username and passwords."
  [request]
  (let [email (-> request :body-params :email)
        password (-> request :body-params :password)
        user (db/get-user-by-email email)
        sanitized-user (dissoc user :password)
        session (:session request)]
    (when (and user (password/check password (:password user)))
      (let [updated-session (assoc session :identity sanitized-user)]
        (-> (rr/redirect "/")
            (assoc :session updated-session))))))

(defn logout [_]
  (-> (rr/redirect "/" :see-other)
      (assoc :session nil)))
