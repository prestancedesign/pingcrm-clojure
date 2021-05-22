(ns pingcrm.router
  (:require [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as bam]
            [inertia.middleware :as inertia]
            [pingcrm.handlers.auth :as auth]
            [pingcrm.handlers.dashboard :as dashboard]
            [pingcrm.handlers.organizations :as organizations]
            [pingcrm.handlers.reports :as reports]
            [pingcrm.handlers.users :as users]
            [pingcrm.middleware.auth :refer [wrap-auth]]
            [pingcrm.middleware.inertia :refer [wrap-inertia-share]]
            [pingcrm.templates.404 :as error]
            [pingcrm.templates.app :refer [template]]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as params]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]))

(def asset-version "1")

(def backend (backends/session))

(defonce session-store (atom {}))

(def config
  {:conflicts nil
   :exception pretty/exception
   :data {:middleware [params/parameters-middleware
                       wrap-keyword-params
                       [wrap-session {:store (memory-store session-store)}]
                       wrap-flash
                       [bam/wrap-authentication backend]
                       wrap-inertia-share
                       [inertia/wrap-inertia template asset-version]]}})
(defn routes [db]
  (ring/ring-handler
   (ring/router
    [["/login"
      {:get  auth/login
       :post {:handler auth/login-authenticate}}]
     ["/logout"
      {:delete {:handler auth/logout}}]
     ["/"
      {:get        dashboard/index
       :middleware [wrap-auth]}]
     ["/users" {:middleware [wrap-auth]}
      [""
       {:get  {:handler users/get-users}
        :post {:handler users/store-user!}}]
      ["/create"
       {:get        users/user-form
        :middleware [wrap-auth]}]
      ["/:user-id"
       {:post   {:handler users/update-user!}
        :delete {:handler users/delete-user!}}]
      ["/:user-id/edit"
       {:get {:handler users/edit-user!}}]
      ["/:user-id/restore"
       {:put {:handler users/restore-user!}}]]
     ["/organizations" {:middleware [wrap-auth]}
      [""
       {:get {:handler (organizations/index db)}}]]
     ["/reports" reports/index]]
    config)
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404, :body error/not-found})}))))
