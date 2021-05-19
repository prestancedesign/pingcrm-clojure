(ns pingcrm.main
  (:require [buddy.auth :as buddy-auth]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as bam]
            [hiccup.page :as page]
            [inertia.middleware :as inertia]
            [pingcrm.db :as db]
            [pingcrm.templates.404 :as error]
            [pingcrm.users.handlers :as users]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as params]
            [ring.adapter.jetty :as server]
            [ring.middleware.flash :refer [wrap-flash]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.util.response :as rr]
            [reitit.dev.pretty :as pretty]
            [crypto.password.bcrypt :as password]))

(def asset-version "1")

(def backend (backends/session))

(defonce session-store (atom {}))

(defn template [data-page]
  (page/html5
   {:class "h-full bg-gray-100"}
   [:head
    [:meta {:charset "utf-8"}]
    [:link {:rel "icon" :type "image/svg+xml" :href "/favicon.svg"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "PingCRM Inertia Clojure"]
    (page/include-css "/css/app.css")
    [:script {:src "/js/ziggy.js"}]
    [:script {:src "/js/app.js" :defer true}]]
   [:body.font-sans.leading-none.text-gray-700.antialiased
    [:div {:id "app"
           :data-page data-page}]]))

(defn login-authenticate
  "Check request username and password against authdata
  username and passwords."
  [request]
  (let [email (-> request :body-params :email)
        password (-> request :body-params :password)
        user (db/get-user-by-email email)
        session (:session request)]
    (when (and user (password/check password (:password user)))
      (let [updated-session (assoc session :identity (dissoc user :password))]
        (-> (rr/redirect "/")
            (assoc :session updated-session))))))

(defn logout [_]
  (-> (rr/redirect "/" :see-other)
      (assoc :session nil)))

(defn wrap-inertia-share [handler]
  (fn [request]
    (let [user-id (-> request :session :identity :id)
          user (db/get-user-by-id user-id)
          success (-> request :flash :success)
          props {:errors {}
                 :auth {:user user}
                 :flash {:success success
                         :error nil}}]
      (handler (assoc request :inertia-share props)))))

(defn auth-middleware
  "Middleware used in routes that require authentication. If request is
  not authenticated a redirect on login page will be executed."
  [handler]
  (fn [request]
    (if (buddy-auth/authenticated? request)
      (handler request)
      (rr/redirect "/login"))))

(def app
  (ring/ring-handler
   (ring/router
    [["/login"
      {:get  (fn [_] (inertia/render "Auth/Login"))
       :post {:handler #'login-authenticate}}]
     ["/logout"
      {:delete {:handler #'logout}}]
     ["/" {:get        (fn [_] (inertia/render "Dashboard/Index"))
           :middleware [auth-middleware]}]
     ["/users" {:middleware [auth-middleware]}
      [""
       {:get {:handler    #'users/get-users
              :parameters {:query {:search int?}}}}]
      ["/:user-id"
       {:post   {:handler    #'users/update-user!
                 :parameters {:body {:first_name string?
                                     :last_name  string?
                                     :email      string?
                                     :owner      boolean?}}}
        :delete {:handler    #'users/delete-user!
                 :parameters {:query {:user-id string?}}}}]
      ["/:user-id/edit"
       {:get {:handler    #'users/edit-user!
              :parameters {:path {:user-id int?}}}}]
      ["/:user-id/restore"
       {:put {:handler    #'users/restore-user!
              :parameters {:path {:user-id int?}}}}]]
     ["/reports" (fn [_] (inertia/render "Reports/Index"))]]
    {:exception pretty/exception
     :data      {:middleware [params/parameters-middleware
                              wrap-keyword-params
                              [wrap-session {:store (memory-store session-store)}]
                              wrap-flash
                              [bam/wrap-authentication backend]
                              wrap-inertia-share
                              [inertia/wrap-inertia template asset-version]]}})
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404, :body error/not-found})}))))

(defn -main []
  (server/run-jetty #'app {:port 3000
                           :join? false}))
