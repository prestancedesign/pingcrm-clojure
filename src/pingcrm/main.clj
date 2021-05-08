(ns pingcrm.main
  (:require [hiccup.page :as page]
            [inertia.middleware :as inertia]
            [pingcrm.templates.error :as error]
            [reitit.ring :as ring]
            [ring.adapter.jetty :as server]))

(def asset-version "1")

(defn template [data-page]
  (page/html5
   {:class "h-full bg-gray-100"}
   [:head
    [:meta {:charset "utf-8"}]
    [:title "PingCRM Inertia Clojure "]
    (page/include-css "/css/app.css")
    [:script {:src "/js/ziggy.js"}]
    [:script {:src "js/app.js" :defer true}]]
   [:body.font-sans.leading-none.text-gray-700.antialiased
    [:div {:id "app"
           :data-page data-page}]]))

(def share
  (let [user {:user {:id 1 :first_name "Mike" :last_name "Salihi" :owner true}}
        account {:id 1 :name "Prestance"}
        auth (assoc-in user [:user :account] account)]
    {:errors {}
     :auth auth
     :flash {:success nil
             :error nil}}))

(defn index [_]
  (inertia/render "Dashboard/Index" {}))

(defn reports [_]
  (inertia/render "Reports/Index" {}))

(defn error []
  (println "error"))

(def app
  (ring/ring-handler
   (ring/router
    [["/" index]
     ["/reports" reports]
     ["/500" error]])
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404, :body error/not-found})}))
   {:middleware [[inertia/wrap-inertia template asset-version share]]}))

(defn -main []
  (server/run-jetty #'app {:port 3000
                           :join? false}))
