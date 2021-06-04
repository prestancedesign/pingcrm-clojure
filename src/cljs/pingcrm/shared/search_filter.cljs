(ns pingcrm.shared.search-filter
  (:require [pingcrm.shared.select-input :refer [select-input]]
            ["@inertiajs/inertia-react" :refer [usePage]]
            ["@inertiajs/inertia" :refer [Inertia]]
            [applied-science.js-interop :as j]
            [reagent.core :as r]
            [goog.functions :refer [throttle]]))

(defn use-effect [values]
  (let [query (clj->js (into {} (filter (comp not-empty val) values)))
        _ (println query)

        options #js {:replace true
                     :preserveState true}]
    (.get Inertia (js/route "organizations") query options)))

(defn search-filter []
  (let [{:keys [filters]} (j/lookup (.-props (usePage)))
        values (r/atom {:search (.-search filters)
                        :role (.-role filters)
                        :trashed (.-trashed filters)})
        handle-change #(let [key (.. % -target -name)
                             value (.. % -target -value)]
                         (swap! values assoc key value)
                         #_(when opened (setOpened false)))
        opened? (r/atom false)]
    (r/create-class
     {:display-name "search-filter"
      :component-did-update
      (fn []
        (throttle (use-effect @values) 150))
      :reagent-render
      (fn []
        (let [reset #(reset! values {})]
          [:div {:class "flex items-center w-full max-w-md mr-4"}
           [:div {:class "relative flex w-full bg-white rounded shadow"}
            [:div {:style {:top "100%"}
                   :class (str "absolute" (when-not @opened? " hidden"))}
             [:div {:class "fixed inset-0 z-20 bg-black opacity-25"
                    :on-click #(reset! opened? false)}]
             [:div {:class "relative z-30 w-64 px-4 py-6 mt-2 bg-white rounded shadow-lg"}
              (when (.hasOwnProperty filters "role")
                [select-input
                 {:class "mb-4"
                  :label "Role"
                  :name "role"
                  :default-value (:role @values)
                  :on-change handle-change}
                 [:option {:value ""}]
                 [:option {:value "user"} "User"]
                 [:option {:value "owner"} "Owner"]])
              [select-input
               {:class "mb-4"
                :label "Trashed"
                :name "trashed"
                :default-value (:trashed @values)
                :on-change handle-change}
               [:option {:value ""}]
               [:option {:value "with"} "With Trashed"]
               [:option {:value "only"} "Only Trashed"]]]]
            [:button {:class "px-4 border-r rounded-l md:px-6 hover:bg-gray-100 focus:outline-none focus:border-white focus:ring-2 focus:ring-indigo-400 focus:z-10"
                      :on-click #(reset! opened? true)}
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
                     :default-value (:search @values)
                     :on-change handle-change
                     :placeholder "Search..."}]]
           [:button {:class "ml-3 text-sm text-gray-600 hover:text-gray-700 focus:text-indigo-700 focus:outline-none"
                     :type "button"
                     :on-click reset}
            "Reset"]]))})))
