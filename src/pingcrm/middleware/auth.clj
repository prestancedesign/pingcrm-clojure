(ns pingcrm.middleware.auth
  (:require [buddy.auth :as buddy-auth]
            [ring.util.response :as rr]))

(defn wrap-auth
  "Middleware used in routes that require authentication. If request is
  not authenticated a redirect on login page will be executed."
  [handler]
  (fn [request]
    (if (buddy-auth/authenticated? request)
      (handler request)
      (rr/redirect "/login"))))
