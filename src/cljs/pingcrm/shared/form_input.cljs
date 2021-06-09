(ns pingcrm.shared.form-input)

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

(defn text-input [{:keys [label name class errors] :as props}]
  [:div {:class class}
   (when label
     [:label.form-label {:html-for name} label ":"])
   [:input (merge props {:id name
                         :name name
                         :class (str "form-input" (when (seq errors) " error"))})]
   (when errors [:div.form-error errors])])
