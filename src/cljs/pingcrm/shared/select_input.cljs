(ns pingcrm.shared.select-input)

(defn select-input
  [{:keys [label name class errors] :as props} & children]
  [:div {:class class}
   (when label
     [:label.form-label {:html-for name} label ":"])
   (into [:select (merge props {:id name
                                :name name
                                :class (str "form-select" (when (seq errors) " error"))})]
         children)
   (when errors
     [:div.form-error errors])])
