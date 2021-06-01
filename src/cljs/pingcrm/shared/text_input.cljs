(ns pingcrm.shared.text-input)

(defn text-input [{:keys [label name class errors] :as props}]
  [:div {:class class}
   (when label
     [:label.form-label {:html-for name} (str label ":")])
   [:input (merge props {:id name
                         :name name
                         :class (str "form-input" (when (seq errors) " error"))})]
   (when errors [:div.form-error errors])])
