(ns pingcrm.templates.app
  (:require [hiccup.page :as page]))

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
