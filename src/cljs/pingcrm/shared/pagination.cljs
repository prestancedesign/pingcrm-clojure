(ns pingcrm.shared.pagination
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [applied-science.js-interop :as j]))

(defn page-link [{:keys [active label url]}]
  [:> InertiaLink {:class (str "mr-1 mb-1 px-4 py-3 text-sm leading-4 border rounded hover:bg-white focus:border-indigo-500 focus:text-indigo-500" (when active " bg-white"))
                   :href url}
   [:label {:dangerouslySetInnerHTML {:__html label}}]])

(defn page-inactive [{:keys [label]}]
  [:div {:class "mr-1 mb-1 px-4 py-3 text-sm border rounded border-solid border-gray-300 text-gray"
         :dangerouslySetInnerHTML {:__html label}}])

(defn pagination [links]
  (when (> (count links) 3)
    [:div {:class "flex flex-wrap mt-6 -mb-1"}
     (for [link links
           :let [{:keys [active label url]} (j/lookup link)]]
       (if (nil? url)
         [page-inactive {:key label :label label}]
         [page-link {:key label :active active :url url :label label}]))]))
