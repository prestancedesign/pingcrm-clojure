(ns pingcrm.shared.top-header
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.logo :refer [logo]]))

(defn top-header []
  [:div
   {:class
    "bg-indigo-900 md:flex-shrink-0 md:w-56 px-6 py-4 flex items-center justify-between md:justify-center"}
   [:> InertiaLink {:class "mt-1" :href "/"}
    [logo {:class "fill-white" :height "28" :width "120"}]]
   [:div {:class "md:hidden"}]])
