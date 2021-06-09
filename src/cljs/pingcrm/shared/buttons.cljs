(ns pingcrm.shared.buttons)

(defn loading-button [{:keys [loading class]} & children]
  (let [class (str class " flex items-center focus:outline-none"
                   (when loading " pointer-events-none bg-opacity-75 select-none"))]
    [:button {:class class
              :disabled loading}
     (when loading [:div.mr-2.btn-spinner])
     children]))

(defn delete-button [{:keys [on-delete]} & children]
  (into
   [:button {:class "text-red-600 focus:outline-none hover:underline"
             :tab-index "-1"
             :type "button"
             :on-click on-delete}]
   children))
