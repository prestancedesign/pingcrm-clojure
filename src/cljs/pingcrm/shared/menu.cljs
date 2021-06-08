(ns pingcrm.shared.menu
  (:require [pingcrm.shared.menu-item :refer [menu-item]]
            ["@inertiajs/inertia-react" :refer [usePage]]
            [applied-science.js-interop :as j]))

(defn main-menu []
  (let [{:keys [url]} (j/lookup (usePage))
        current-url (subs url 1)]
    [:div {:class "flex-shrink-0 hidden w-56 p-12 overflow-y-auto bg-indigo-800 md:block"}
     [menu-item {:text "Dashboard" :link "dashboard" :icon-name :dashboard :current-url current-url}]
     [menu-item {:text "Organizations" :link "organizations" :icon-name :office :current-url current-url}]
     [menu-item {:text "Contacts" :link "contacts" :icon-name :users :current-url current-url}]
     [menu-item {:text "Reports" :link "reports" :icon-name :printer :current-url current-url}]]))
