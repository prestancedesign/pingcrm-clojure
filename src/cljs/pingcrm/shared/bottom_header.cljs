(ns pingcrm.shared.bottom-header
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink usePage]]
            ["react" :as react]
            [applied-science.js-interop :as j]
            [pingcrm.shared.icon :refer [icon]]))

(defn bottom-header []
  (let [{:keys [^js auth]} (j/lookup (.-props (usePage)))
        user (.. auth -user)
        [opened? set-opened] (react/useState false)]
    [:div
     {:class
      "bg-white border-b w-full p-4 md:py-0 md:px-12 text-sm md:text-md flex justify-between items-center"}
     [:div {:class "mt-1 mr-4"} (.. user -account -name)]
     [:div {:class "relative"}
      [:div
       {:class "flex items-center cursor-pointer select-none group",
        :on-click #(set-opened not)}
       [:div
        {:class
         "mr-1 text-gray-800 whitespace-nowrap group-hover:text-indigo-600 focus:text-indigo-600"}
        [:span (.-first_name user)]
        [:span {:class "hidden ml-1 md:inline"} (.-last_name user)]]
       [icon
        {:class
         "w-5 h-5 text-gray-800 fill-current group-hover:text-indigo-600 focus:text-indigo-600",
         :name :cheveron-down}]]
      [:div {:class (when-not opened? "hidden")}
       [:div
        {:class
         "absolute top-0 right-0 left-auto z-20 py-2 mt-8 text-sm whitespace-nowrap bg-white rounded shadow-xl"}
        [:> InertiaLink
         {:href (str "/users/" (.. user -id) "/edit"),
          :class "block px-6 py-2 hover:bg-indigo-600 hover:text-white",
          :on-click #(set-opened not)} "My Profile"]
        [:> InertiaLink
         {:href "/users",
          :class "block px-6 py-2 hover:bg-indigo-600 hover:text-white",
          :on-click #(set-opened not)} "Manage Users"]
        [:> InertiaLink
         {:as "button",
          :href "/logout",
          :class
          "block w-full px-6 py-2 text-left focus:outline-none hover:bg-indigo-600 hover:text-white",
          :method "delete"} "Logout"]]
       [:div
        {:class "fixed inset-0 z-10 bg-black opacity-25",
         :on-click #(set-opened not)}]]]]))
