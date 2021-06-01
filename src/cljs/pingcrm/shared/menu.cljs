(ns pingcrm.shared.menu
  (:require [pingcrm.shared.menu-item :refer [menu-item]]))

(defn main-menu []
  [:div {:class "flex-shrink-0 hidden w-56 p-12 overflow-y-auto bg-indigo-800 md:block"}
   [menu-item {:text "Dashboard" :link "dashboard" :icon-name :dashboard}]
   [menu-item {:text "Organizations" :link "organizations" :icon-name :office}]
   [menu-item {:text "Contacts" :link "contacts" :icon-name :users}]
   [menu-item {:text "Reports" :link "reports" :icon-name :printer}]])
