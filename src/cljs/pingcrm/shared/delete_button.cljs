(ns pingcrm.shared.delete-button)

(defn delete-button [{:keys [on-delete]} & children]
  (into
   [:button {:class "text-red-600 focus:outline-none hover:underline"
             :tab-index "-1"
             :type "button"
             :on-click on-delete}]
   children))
