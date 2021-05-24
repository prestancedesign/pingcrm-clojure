(ns pingcrm.router
  (:require [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as bam]
            [inertia.middleware :as inertia]
            [pingcrm.handlers.auth :as auth]
            [pingcrm.handlers.contacts :as contacts]
            [pingcrm.handlers.dashboard :as dashboard]
            [pingcrm.handlers.organizations :as organizations]
            [pingcrm.handlers.reports :as reports]
            [pingcrm.handlers.users :as users]
            [pingcrm.middleware.auth :refer [wrap-auth]]
            [pingcrm.middleware.inertia :refer [wrap-inertia-share]]
            [pingcrm.templates.404 :as error]
            [pingcrm.templates.app :refer [template]]
            [reitit.coercion.schema :as schema-coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.parameters :as params]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [schema.core :as s]))

(def asset-version "1")

(def backend (backends/session))

(def cookie-store-secret "x&Hvnhx7BqYalmQr")

(defn config [db]
  {:conflicts nil
   :exception pretty/exception
   :data {:coercion   schema-coercion/coercion
          :db db
          :middleware [params/parameters-middleware
                       rrc/coerce-exceptions-middleware
                       rrc/coerce-request-middleware
                       rrc/coerce-response-middleware
                       wrap-keyword-params
                       [wrap-session {:store (cookie-store {:key cookie-store-secret})}]
                       wrap-flash
                       [bam/wrap-authentication backend]
                       wrap-inertia-share
                       [inertia/wrap-inertia template asset-version]]}})

(defn routes [db]
  (ring/ring-handler
   (ring/router
    [["/login"
      {:get  auth/login
       :post {:handler (auth/login-authenticate db)}}]
     ["/logout"
      {:delete {:handler auth/logout}}]
     ["/"
      {:get        dashboard/index
       :middleware [wrap-auth]}]
     ;; Users routes
     ["/users" {:middleware [wrap-auth]}
      [""
       {:get  {:handler (users/get-users db)}
        :post {:handler (users/store-user! db)}}]
      ["/create"
       {:get users/user-form}]
      ["/:user-id"
       {:post   {:handler (users/update-user! db)}
        :delete {:handler (users/delete-user! db)}}]
      ["/:user-id/edit"
       {:get {:handler (users/edit-user! db)}}]
      ["/:user-id/restore"
       {:put {:handler (users/restore-user! db)}}]]
     ;; Organizations routes
     ["/organizations" {:middleware [wrap-auth]}
      [""
       {:get  {:handler    (organizations/index db)
               :parameters {:query {(s/optional-key :page) Long}}}
        :post {:handler (organizations/store-organization! db)}}]
      ["/create"
       {:get organizations/organization-form}]
      ["/:organization-id"
       {:put    {:handler (organizations/update-organization! db)}
        :delete {:handler (organizations/delete-organization! db)}}]
      ["/:organization-id/edit"
       {:get {:handler (organizations/edit-organization! db)}}]
      ["/:organization-id/restore"
       {:put {:handler (organizations/restore-organization! db)}}]]
     ;; Contacts routes
     ["/contacts" {:middleware [wrap-auth]}
      [""
       {:get  {:handler    (contacts/index db)
               :parameters {:query {(s/optional-key :page) Long}}}
        :post {:handler (contacts/store-contact! db)}}]
      ["/create"
       {:get contacts/contacts-form}]
      ["/:contact-id"
       {:put    {:handler (contacts/update-contact! db)}
        :delete {:handler (contacts/delete-contact! db)}}]
      ["/:contact-id/edit"
       {:get {:handler (contacts/edit-contact! db)}}]
      ["/:contact-id/restore"
       {:put {:handler (contacts/restore-contact! db)}}]]
     ["/reports" reports/index]]
    (config db))
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404, :body error/not-found})}))))
