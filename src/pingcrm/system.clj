(ns pingcrm.system
  (:require [pingcrm.router :as router]
            [ring.adapter.jetty :as server]))

(def app
  router/routes)

(defn -main []
  (server/run-jetty #'app {:port 3000
                           :join? false}))
