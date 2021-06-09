(ns pingcrm.pages.reports
  (:require [pingcrm.shared.site-head :refer [site-head]]))

(defn index []
  [:div
   [site-head {:title "Reports"}]
   [:h1 {:class "mb-8 text-3xl font-bold"} "Reports"]])
