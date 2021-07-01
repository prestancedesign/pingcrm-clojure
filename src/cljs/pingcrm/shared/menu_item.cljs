(ns pingcrm.shared.menu-item
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [pingcrm.shared.icon :refer [icon]]
            [clojure.string :as str]))

(defn menu-item [{:keys [text link icon-name current-url]}]
  (let [active? (or (and (= current-url "") (= link "dashboard")) (str/starts-with? current-url link))
        icon-class (str "w-4 h-4 mr-2 " (if active? "text-white fill-current" "text-indigo-400 group-hover:text-white fill-current"))
        text-class (if active? "text-white" "text-indigo-300 group-hover:text-white")]
    [:div {:class "mb-4"}
     [:> InertiaLink {:href (str "/" (when-not (= link "dashboard") link)) :class "flex items-center group py-3"}
      [icon {:name icon-name :class icon-class}]
      [:div {:class text-class}
       text]]]))
