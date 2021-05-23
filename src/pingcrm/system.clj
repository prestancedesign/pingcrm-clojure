(ns pingcrm.system
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [pingcrm.router :as router]
            [ring.adapter.jetty :as server]))

(defn app [db]
  (router/routes db))

(def config
  {:server/jetty {:handler (ig/ref :pingcrm/app) :port 3000}
   :pingcrm/app {:db (ig/ref :database.sql/connection)}
   :database.sql/connection {:dbtype "sqlite" :dbname "database/database.sqlite"}})

(defmethod ig/init-key :server/jetty [_ {:keys [handler port]}]
  (println "\nServer running on port" port)
  (server/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :pingcrm/app [_ {:keys [db]}]
  (app db))

(defmethod ig/init-key :database.sql/connection [_ db-spec]
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/with-options ds {:builder-fn rs/as-unqualified-maps})))

(defmethod ig/halt-key! :server/jetty [_ server]
  (.stop server))

(defn -main []
  (ig/init config))
