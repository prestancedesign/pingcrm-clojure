(ns pingcrm.shared.loading-button)

(defn loading-button [{:keys [loading class]} & children]
  (let [class (str class " flex items-center focus:outline-none"
                   (when loading " pointer-events-none bg-opacity-75 select-none"))]
    [:button {:class class
              :disabled loading}
     (when loading [:div.mr-2.btn-spinner])
     children]))
