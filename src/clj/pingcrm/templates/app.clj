(ns pingcrm.templates.app
  (:require [clojure.edn :as edn]
            [hiccup.util :as h]
            [hiccup.page :as page]
            [jsonista.core :as json]
            [org.httpkit.client :as http]))

(defn js-script []
  (-> (slurp "public/js/manifest.edn")
      edn/read-string
      first
      :output-name))

(defn template [data-page]
  (let [render @(http/post "http://localhost:8000/render" {:body data-page
                                                           :headers {"Content-Type" "application/json"}})
        {:keys [body head]} (json/read-value (:body render) json/keyword-keys-object-mapper)]
    (page/html5
     {:class "h-full bg-gray-100"}
     [:head
      [:meta {:charset "utf-8"}]
      [:link {:rel "icon" :type "image/svg+xml" :href "/favicon.svg"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      (page/include-css "/css/app.css")
      [:script {:src (str "/js/" (js-script)) :defer true}]
      (for [h head]
        (h/as-str h))]
     [:body.font-sans.leading-none.text-gray-700.antialiased
      (if (seq body)
        body
        [:div {:id "app"
               :data-page data-page}])])))
