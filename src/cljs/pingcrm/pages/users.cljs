(ns pingcrm.pages.users
  (:require [pingcrm.shared.search-filter :refer [search-filter]]
            ["@inertiajs/inertia-react" :refer [InertiaLink]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.icon :refer [icon]]))

(defn index [{:keys [users]}]
  [:div
   [:h1 {:class "mb-8 text-3xl font-bold"} "Users"]
   [:div {:class "flex items-center justify-between mb-6"}
    [:f> search-filter]
    [:> InertiaLink {:class "btn-indigo focus:outline-none"
                     :href (js/route "users.create")}
     [:span "Create"]
     [:span {:class "hidden md:inline"} " User"]]]
   [:div {:class "overflow-x-auto bg-white rounded shadow"}
    [:table {:class "w-full whitespace-nowrap"}
     [:thead
      [:tr {:class "font-bold text-left"}
       [:th {:class "px-6 pt-5 pb-4"} "Name"]
       [:th {:class "px-6 pt-5 pb-4"} "Email"]
       [:th {:class "px-6 pt-5 pb-4" :col-span "2"} "Role"]]]
     [:tbody
      (for [user users
            :let [{:keys [id name email owner deleted_at]} (j/lookup user)]]
        [:tr {:class "hover:bg-gray-100 focus-within:bg-gray-100"
              :key id}
         [:td {:class "border-t"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           name
           (when deleted_at
             [icon {:name :trash
                    :class "flex-shrink-0 w-3 h-3 ml-2 text-gray-400 fill-current"}])]]
         [:td {:class "border-t"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           email]]
         [:td {:class "border-t"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           (if owner "Owner" "User")]]
         [:td {:class "w-px border-t"}
          [:> InertiaLink {:href (js/route "users.edit" id)
                           :tab-index "-1"
                           :class "flex items-center px-4 focus:outline-none"}
           [icon {:name :cheveron-right
                  :class "block w-6 h-6 text-gray-400 fill-current"}]]]])
      (when (zero? (count users))
        [:tr
         [:td {:class "px-6 py-4 border-t"
               :col-span "4"}
          "No contacts found."]])]]]])
