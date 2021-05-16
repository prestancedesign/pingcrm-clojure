(ns pingcrm.templates.404
  (:require [hiccup.page :as page]))

(def not-found
  (page/html5
   {:class "h-full bg-gray-100"}
   [:head
    [:meta {:charset "utf-8"}]
    [:link {:rel "icon" :type "image/svg+xml" :href "/favicon.svg"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "404 | Not found"]
    (page/include-css "/css/app.css")]
   [:body.antialiased
    [:div.relative.flex.items-top.justify-center.min-h-screen.bg-gray-100.dark:bg-gray-900.sm:items-center.sm:pt-0
     [:div.max-w-xl.mx-auto.sm:px-6.lg:px-8
      [:div.flex.items-center.pt-8.sm:justify-start.sm:pt-0
       [:div.px-4.text-lg.text-gray-500.border-r.border-gray-400.tracking-wider
        "404"]
       [:div.ml-4.text-lg.text-gray-500.uppercase.tracking-wider "Not Found"]]]]]))
