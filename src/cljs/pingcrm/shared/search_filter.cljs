(ns pingcrm.shared.search-filter
  (:require ["@inertiajs/inertia" :refer [Inertia]]
            ["@inertiajs/inertia-react" :refer [usePage]]
            [applied-science.js-interop :as j]
            [goog.functions :refer [throttle]]
            [pingcrm.shared.form-input :refer [select-input]]
            ["react" :as react]))

(defn search-filter []
  (let [{:keys [filters]} (j/lookup (.-props (usePage)))
        [values set-values] (react/useState {:search (or (.-search filters) "")
                                             :role (or (.-role filters) "")
                                             :trashed (or (.-trashed filters) "")})
        [opened? set-opened] (react/useState false)
        on-change #(let [key (keyword (.. % -target -name))
                         value (.. % -target -value)]
                     (throttle (set-values (fn [val] (assoc val key value))) 150)
                     (when opened? (set-opened false)))
        reset #(set-values {:search ""
                            :role ""
                            :trashed ""})]

    (react/useEffect
     (fn []
       (let [query (into {} (filter (comp not-empty val) values))
             options #js {:replace true
                          :preserveState true}]
         (.get Inertia (.. js/window -location -pathname) (clj->js query) options)))
     #js [values])

    [:div {:class "flex items-center w-full max-w-md mr-4"}
     [:div {:class "relative flex w-full bg-white rounded shadow"}
      [:div {:style {:top "100%"}
             :class (str "absolute" (when-not opened? " hidden"))}
       [:div {:class "fixed inset-0 z-20 bg-black opacity-25"
              :on-click #(set-opened false)}]
       [:div {:class "relative z-30 w-64 px-4 py-6 mt-2 bg-white rounded shadow-lg"}
        (when (.hasOwnProperty filters "role")
          [select-input
           {:class "mb-4"
            :label "Role"
            :name "role"
            :value (:role values)
            :on-change on-change}
           [:option {:value ""}]
           [:option {:value "user"} "User"]
           [:option {:value "owner"} "Owner"]])
        [select-input
         {:class "mb-4"
          :label "Trashed"
          :name "trashed"
          :value (:trashed values)
          :on-change on-change}
         [:option {:value ""}]
         [:option {:value "with"} "With Trashed"]
         [:option {:value "only"} "Only Trashed"]]]]
      [:button {:class "px-4 border-r rounded-l md:px-6 hover:bg-gray-100 focus:outline-none focus:border-white focus:ring-2 focus:ring-indigo-400 focus:z-10"
                :on-click #(set-opened true)}
       [:div {:class "flex items-baseline"}
        [:span {:class "hidden text-gray-700 md:inline"} "Filter"]
        [:svg {:class "w-2 h-2 text-gray-700 fill-current md:ml-2"
               :xmlns "http://www.w3.org/2000/svg"
               :view-box "0 0 961.243 599.998"}
         [:path {:d "M239.998 239.999L0 0h961.243L721.246 240c-131.999 132-240.28 240-240.624 239.999-.345-.001-108.625-108.001-240.624-240z"}]]]]
      [:input {:class "relative w-full px-6 py-3 rounded-r focus:outline-none focus:ring-2 focus:ring-indigo-400"
               :auto-complete "off"
               :type "text"
               :name "search"
               :value (:search values)
               :on-change on-change
               :placeholder "Search..."}]]
     [:button {:class "ml-3 text-sm text-gray-600 hover:text-gray-700 focus:text-indigo-700 focus:outline-none"
               :type "button"
               :on-click reset}
      "Reset"]]))
