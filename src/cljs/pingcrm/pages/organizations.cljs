(ns pingcrm.pages.organizations
  (:require ["@inertiajs/inertia-react" :refer [InertiaLink useForm]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.icon :refer [icon]]
            [pingcrm.shared.loading-button :refer [loading-button]]
            [pingcrm.shared.pagination :refer [pagination]]
            [pingcrm.shared.search-filter :refer [search-filter]]
            [pingcrm.shared.select-input :refer [select-input]]
            [pingcrm.shared.text-input :refer [text-input]]))

(defn index
  [{:keys [organizations]}]
  (let [{:keys [data links]} (j/lookup organizations)]
    [:div
     [:h1 {:class "mb-8 text-3xl font-bold"} "Organizations"]
     [:div {:class "flex items-center justify-between mb-6"}
      [:f> search-filter]
      [:> InertiaLink
       {:class "btn-indigo focus:outline-none",
        :href (js/route "organizations.create")} [:span "Create "]
       [:span {:class "hidden md:inline"} "Organization"]]]
     [:div {:class "overflow-x-auto bg-white rounded shadow"}
      [:table {:class "w-full whitespace-nowrap"}
       [:thead
        [:tr {:class "font-bold text-left"}
         [:th {:class "px-6 pt-5 pb-4"} "Name"]
         [:th {:class "px-6 pt-5 pb-4"} "City"]
         [:th {:class "px-6 pt-5 pb-4", :col-span "2"} "Phone"]]]
       [:tbody
        (for [organization data
              :let [{:keys [id name city phone deleted_at]} (j/lookup organization)]]
          [:tr {:class "hover:bg-gray-100 focus-within:bg-gray-100"
                :key id}
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "organizations.edit" id)
                             :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
             name
             (when deleted_at
               [icon {:name :trash
                      :class "flex-shrink-0 w-3 h-3 ml-2 text-gray-400 fill-current"}])]]
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "organizations.edit" id)
                             :tab-index "-1"
                             :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
             city]]
           [:td {:class "border-t"}
            [:> InertiaLink {:href (js/route "organizations.edit" id)
                             :tab-index "-1"
                             :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
             phone]]
           [:td {:class "w-px border-t"}
            [:> InertiaLink {:href (js/route "organizations.edit" id)
                             :tab-index "-1"
                             :class "flex items-center px-4 focus:outline-none"}
             [icon {:name :cheveron-right
                    :class "block w-6 h-6 text-gray-400 fill-current"}]]]])
        (when (zero? (count data))
          [:tr
           [:td {:class "px-6 py-4 border-t"
                 :col-span "4"}
            "No organizations found."]])]]]
     [pagination links]]))

(defn create-form []
  (let [{:keys [data setData errors post processing]} (j/lookup
                                                       (useForm #js {:name ""
                                                                     :email ""
                                                                     :phone ""
                                                                     :address ""
                                                                     :city ""
                                                                     :region ""
                                                                     :country ""
                                                                     :postal_code ""}))
        on-submit #(do (.preventDefault %)
                       (post (js/route "organizations.store")))]
    [:div
     [:h1 {:class "mb-8 text-3xl font-bold"}
      [:> InertiaLink {:href (js/route "organizations")
                       :class "text-indigo-400 hover:text-indigo-600"}
       "Organizations"]
      [:span {:class ""} " / "]
      "Create"]
     [:div {:class "max-w-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-wrap p-8 -mb-8 -mr-6"}
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Name"
                     :name "name"
                     :errors (.-name errors)
                     :value (.-name data)
                     :on-change #(setData "name" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Phone"
                     :name "phone"
                     :errors (.-phone errors)
                     :value (.-phone data)
                     :on-change #(setData "phone" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Address"
                     :name "address"
                     :errors (.-address errors)
                     :value (.-address data)
                     :on-change #(setData "address" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "City"
                     :name "city"
                     :errors (.-city errors)
                     :value (.-city data)
                     :on-change #(setData "city" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Province/State"
                     :name "region"
                     :errors (.-region errors)
                     :value (.-region data)
                     :on-change #(setData "region" (.. % -target -value))}]
        [select-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                       :label "Country"
                       :name "country"
                       :errors (.-country errors)
                       :value (.-country data)
                       :on-change #(setData "country" (.. % -target -value))}
         [:option {:value ""}]
         [:option {:value "CA"} "Canada"]
         [:option {:value "US"} "United States"]]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Postal code"
                     :name "postal_code"
                     :errors (.-postal_code errors)
                     :value (.-postal_code data)
                     :on-change #(setData "postal_code" (.. % -target -value))}]]
       [:div {:class "px-8 py-4 bg-gray-50 border-t border-gray-100 flex justify-end items-center"}
        [loading-button {:loading processing
                         :type "submit"
                         :class "btn-indigo"}
         "Create Organization"]]]]]))

(defn create []
  [:f> create-form])
