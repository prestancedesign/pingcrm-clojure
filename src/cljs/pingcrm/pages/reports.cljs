(ns pingcrm.pages.reports
  (:require ["@inertiajs/inertia-react" :refer [Head]]))

(defn index []
  [:div
   [:> Head {:title "Reports"}]
   [:h1 {:class "mb-8 text-3xl font-bold"} "Reports"]])
