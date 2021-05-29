(ns pingcrm.shared.layout
  (:require [pingcrm.shared.logo :refer [logo]]))

(defn layout [children]
  [:div [:portal-target {:name "dropdown", :slim ""}]
   [:div.md:flex.md:flex-col
    [:div.md:h-screen.md:flex.md:flex-col
     [:div.md:flex.md:flex-shrink-0
      [:div.bg-indigo-900.md:flex-shrink-0.md:w-56.px-6.py-4.flex.items-center.justify-between.md:justify-center
       [:inertia-link.mt-1 {:href "/"}
        [logo]]
       [:dropdown.md:hidden {:placement "bottom-end"}
        [:svg.fill-white.w-6.h-6
         {:viewbox "0 0 20 20", :xmlns "http://www.w3.org/2000/svg"}
         [:path {:d "M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"}]]
        [:div.mt-2.px-8.py-4.shadow-lg.bg-indigo-800.rounded {:slot "dropdown"}
         [:main-menu]]]]
      [:div.bg-white.border-b.w-full.p-4.md:py-0.md:px-12.text-sm.md:text-md.flex.justify-between.items-center
       [:div.mt-1.mr-4 "{{ $page.props.auth.user.account.name }}"]
       [:dropdown.mt-1 {:placement "bottom-end"}
        [:div.flex.items-center.cursor-pointer.select-none.group
         [:div.text-gray-700.group-hover:text-indigo-600.focus:text-indigo-600.mr-1.whitespace-nowrap
          [:span "{{ $page.props.auth.user.first_name }}"]
          [:span.hidden.md:inline "{{ $page.props.auth.user.last_name }}"]]
         [:icon.w-5.h-5.group-hover:fill-indigo-600.fill-gray-700.focus:fill-indigo-600
          {:name "cheveron-down"}]]
        [:div.mt-2.py-2.shadow-xl.bg-white.rounded.text-sm {:slot "dropdown"}
         [:inertia-link.block.px-6.py-2.hover:bg-indigo-500.hover:text-white
          {::href "route('users.edit', $page.props.auth.user.id)"} "My Profile"]
         [:inertia-link.block.px-6.py-2.hover:bg-indigo-500.hover:text-white
          {::href "route('users')"} "Manage Users"]
         [:inertia-link.block.px-6.py-2.hover:bg-indigo-500.hover:text-white.w-full.text-left
          {:as "button", ::href "route('logout')", :method "delete"} "Logout"]]]]]
     [:div.md:flex.md:flex-grow.md:overflow-hidden
      [:main-menu.hidden.md:block.bg-indigo-800.flex-shrink-0.w-56.p-12.overflow-y-auto]
      [:div.md:flex-1.px-4.py-8.md:p-12.md:overflow-y-auto {:scroll-region ""}
       [:flash-messages] children]]]]])
