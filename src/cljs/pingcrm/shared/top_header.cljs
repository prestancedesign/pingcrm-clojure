(ns pingcrm.shared.top-header
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.logo :refer [logo]]
            [pingcrm.shared.menu :refer [main-menu]]
            [reagent.core :as r]))

(defn top-header []
  (let [opened? (r/atom false)
        on-click #(reset! opened? false)]
    (fn []
      [:div {:class "bg-indigo-900 md:flex-shrink-0 md:w-56 px-6 py-4 flex items-center justify-between md:justify-center"}
       [:> InertiaLink {:class "mt-1", :href "/"}
        [logo {:class "fill-white", :height "28", :width "120"}]]
       [:div {:class "relative md:hidden"}
        [:svg {:class "w-6 h-6 text-white cursor-pointer fill-current"
               :on-click #(reset! opened? true)
               :xmlns "http://www.w3.org/2000/svg"
               :view-box "0 0 20 20"}
         [:path {:d "M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"}]]
        [:div {:class (str "absolute right-0 z-20" (when-not @opened? " hidden"))
               :on-click on-click}
         [:f> main-menu {:class "relative z-20 px-8 py-4 pb-2 mt-2 bg-indigo-800 rounded shadow-lg"}]
         [:div {:on-click on-click
                :class "fixed inset-0 z-10 bg-black opacity-25"}]]]])))
