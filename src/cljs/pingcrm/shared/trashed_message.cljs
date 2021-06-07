(ns pingcrm.shared.trashed-message
  (:require [pingcrm.shared.icon :refer [icon]]))

(defn trashed-message [{:keys [on-restore]} & children]
  [:div {:class "p-4 bg-yellow-300 rounded flex items-center justify-between max-w-3xl mb-6"}
   [:div {:class "flex items-center"}
    [icon {:name :trash
           :class "flex-shrink-0 w-4 h-4 fill-current text-yellow-800 mr-2"}]
    (into
     [:div {:class "text-sm font-medium text-yellow-800"}]
     children)]
   [:button {:class "text-yellow-800 focus:outline-none text-sm hover:underline"
             :tab-index "-1"
             :type "button"
             :on-click on-restore}
    "Restore"]])
