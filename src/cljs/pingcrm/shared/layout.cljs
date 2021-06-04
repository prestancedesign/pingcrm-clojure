(ns pingcrm.shared.layout
  (:require [pingcrm.shared.menu :refer [main-menu]]
            [pingcrm.shared.top-header :refer [top-header]]
            [pingcrm.shared.bottom-header :refer [bottom-header]]))

(defn layout
  [children]
  [:div
   ;[:helmet {:title "{title}", :titletemplate "%s | Ping CRM"}]
   [:div {:class "md:flex md:flex-col"}
    [:div {:class "md:h-screen md:flex md:flex-col"}
     [:div {:class "md:flex md:flex-shrink-0"}
      [top-header]
      [:f> bottom-header]]
     [:div {:class "flex flex-grow overflow-hidden"}
      [main-menu
       {:class
          "flex-shrink-0 hidden w-56 p-12 overflow-y-auto bg-indigo-800 md:block"}]
      ;; To reset scroll region (https://inertiajs.com/pages#scroll-regions) add
      ;; `scroll-region="true"` to div below
      [:div {:class "w-full px-4 py-8 overflow-hidden overflow-y-auto md:p-12"}
       ;;[:flashmessages]
       children]]]]])
