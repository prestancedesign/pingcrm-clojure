(ns pingcrm.middleware.inertia
  (:require [pingcrm.models.users :as db]))

(defn wrap-inertia-share [handler]
  (fn [request]
    (let [user-id (-> request :session :identity :id)
          user (db/get-user-by-id user-id)
          success (-> request :flash :success)
          errors (-> request :flash :error)
          props {:errors (or errors {})
                 :auth {:user user}
                 :flash {:success success
                         :error nil}}]
      (handler (assoc request :inertia-share props)))))
