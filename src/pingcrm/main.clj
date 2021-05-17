(ns pingcrm.main
  (:require [hiccup.page :as page]
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
            [ring.middleware.session.memory :refer [memory-store]]))

(def asset-version "1")

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

(defn wrap-inertia-share [handler]
  (fn [request]
    (let [user (db/get-user-by-id 1)
          success (-> request :flash :success)
          props {:errors {}
                 :auth {:user user}
                 :flash {:success success
                         :error nil}}]
      (handler (assoc request :inertia-share props)))))

(defn index [_]
  (inertia/render "Dashboard/Index"))

(defn reports [_]
  (inertia/render "Reports/Index"))

(def app
  (ring/ring-handler
   (ring/router
    [["/" {:get {:handler #'index}}]
     ["/users"
      [""
       {:get {:handler    #'users/get-users
              :parameters {:query {:search int?}}}}]
      ["/:user-id"
       {:post {:handler    #'users/update-user!
               :parameters {:body {:first_name string?
                                   :last_name  string?
                                   :email      string?
                                   :owner      boolean?}}}}]
      ["/:user-id/edit"
       {:get {:handler    #'users/edit-user!
              :parameters {:query {:user-id int?}}}}]]
     ["/reports" {:get {:handler #'reports}}]]
    {:data {:middleware [params/parameters-middleware
                         wrap-keyword-params
                         [wrap-session {:store (memory-store)}]
                         wrap-flash
                         wrap-inertia-share
                         [inertia/wrap-inertia template asset-version]]}})
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404, :body error/not-found})}))))

(defn -main []
  (server/run-jetty #'app {:port 3000
                           :join? false}))
