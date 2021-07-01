(ns pingcrm.pages.users
  (:require ["@inertiajs/inertia" :refer [Inertia]]
            ["@inertiajs/inertia-react" :refer [InertiaLink useForm]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.buttons :refer [loading-button delete-button]]
            [pingcrm.shared.form-input :refer [text-input select-input]]
            [pingcrm.shared.icon :refer [icon]]
            [pingcrm.shared.search-filter :refer [search-filter]]
            [pingcrm.shared.site-head :refer [site-head]]
            [pingcrm.shared.trashed-message :refer [trashed-message]]))

(defn index [{:keys [users]}]
  [:div
   [site-head {:title "Users"}]
   [:h1 {:class "mb-8 text-3xl font-bold"} "Users"]
   [:div {:class "flex items-center justify-between mb-6"}
    ;; TODO: Fix search box for ssr
    ;; [:f> search-filter]
    [:> InertiaLink {:class "btn-indigo focus:outline-none"
                     :href "/users/create"}
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
          [:> InertiaLink {:href (str "/users/" id "/edit")
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           name
           (when deleted_at
             [icon {:name :trash
                    :class "flex-shrink-0 w-3 h-3 ml-2 text-gray-400 fill-current"}])]]
         [:td {:class "border-t"}
          [:> InertiaLink {:href (str "/users/" id "/edit")
                           :tab-index "-1"
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           email]]
         [:td {:class "border-t"}
          [:> InertiaLink {:href (str "/users/" id "/edit")
                           :tab-index "-1"
                           :class "flex items-center px-6 py-4 focus:text-indigo-700 focus:outline-none"}
           (if owner "Owner" "User")]]
         [:td {:class "w-px border-t"}
          [:> InertiaLink {:href (str "/users/" id "/edit")
                           :tab-index "-1"
                           :class "flex items-center px-4 focus:outline-none"}
           [icon {:name :cheveron-right
                  :class "block w-6 h-6 text-gray-400 fill-current"}]]]])
      (when (zero? (count users))
        [:tr
         [:td {:class "px-6 py-4 border-t"
               :col-span "4"}
          "No Users found."]])]]]])

(defn create-form []
  (let [{:keys [data setData errors post processing]}
        (j/lookup (useForm #js {:first_name ""
                                :last_name ""
                                :email ""
                                :password ""
                                :owner "0"}))
        on-submit #(do (.preventDefault %)
                       (post "/users"))]
    [:div
     [site-head {:title "Create User"}]
     [:h1 {:class "mb-8 text-3xl font-bold"}
      [:> InertiaLink {:href "/users"
                       :class "text-indigo-400 hover:text-indigo-600"}
       "Users" [:span {:class "font-medium text-indigo-400"} " / "]]
      "Create"]
     [:div {:class "max-w-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-wrap p-8 -mb-8 -mr-6"}
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "First name"
                     :name "first_name"
                     :errors (.-first_name errors)
                     :value (.-first_name data)
                     :on-change #(setData "first_name" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Last name"
                     :name "last_name"
                     :errors (.-last_name errors)
                     :value (.-last_name data)
                     :on-change #(setData "last_name" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Password"
                     :name "password"
                     :type "password"
                     :errors (.-password errors)
                     :value (.-password data)
                     :on-change #(setData "password" (.. % -target -value))}]
        [select-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                       :label "Owner"
                       :name "owner"
                       :errors (.-owner errors)
                       :value (.-owner data)
                       :on-change #(setData "country" (.. % -target -value))}
         [:option {:value "1"} "Yes"]
         [:option {:value "0"} "No"]]]
       [:div {:class "px-8 py-4 bg-gray-50 border-t border-gray-100 flex justify-end items-center"}
        [loading-button {:loading processing
                         :type "submit"
                         :class "btn-indigo"}
         "Create User"]]]]]))

(defn edit-form [^js user]
  (let [{:keys [data setData errors post processing]}
        (j/lookup (useForm #js {:first_name (or (.-first_name user) "")
                                :last_name (or (.-last_name user) "")
                                :email (or (.-email user) "")
                                :password (or (.-password user) "")
                                :owner (or (if (.-owner user) "1" "0") "0")}))
        on-submit #(do (.preventDefault %)
                       (post (str "/users/" (.-id user))))
        destroy #(when (js/confirm "Are you sure you want to delete this user?")
                   (.delete Inertia (str "/users/" (.-id user))))
        restore #(when (js/confirm "Are you sure you want to restore this user?")
                   (.put Inertia (str "/users/" (.-id user) "/restore")))]
    [:<>
     [site-head {:title (str (j/get user :first_name) " " (j/get user :last_name))}]
     [:div {:class "flex justify-start max-w-lg mb-8"}
      [:h1 {:class "text-3xl font-bold"}
       [:> InertiaLink {:class "text-indigo-400 hover:text-indigo-700"
                        :href "/users"} "Users"]
       [:span {:class "mx-2 font-medium text-indigo-400"} "/"]
       (.-first_name user) " " (.-last_name user)]]
     (when-not (empty? (j/get user :deleted_at))
       [trashed-message {:on-restore restore}
        "This user has been deleted."])
     [:div {:class "max-w-3xl overflow-hidden bg-white rounded shadow"}
      [:form {:on-submit on-submit}
       [:div {:class "flex flex-wrap p-8 -mb-8 -mr-6"}
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "First name"
                     :name "first_name"
                     :errors (.-first_name errors)
                     :value (.-first_name data)
                     :on-change #(setData "first_name" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Last name"
                     :name "last_name"
                     :errors (.-last_name errors)
                     :value (.-last_name data)
                     :on-change #(setData "last_name" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Email"
                     :name "email"
                     :errors (.-email errors)
                     :value (.-email data)
                     :on-change #(setData "email" (.. % -target -value))}]
        [text-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                     :label "Password"
                     :name "password"
                     :type "password"
                     :errors (.-password errors)
                     :value (.-password data)
                     :on-change #(setData "password" (.. % -target -value))}]
        [select-input {:class "w-full pb-8 pr-6 lg:w-1/2"
                       :label "Owner"
                       :name "owner"
                       :errors (.-owner errors)
                       :value (.-owner data)
                       :on-change #(setData "owner" (.. % -target -value))}
         [:option {:value "1"} "Yes"]
         [:option {:value "0"} "No"]]]
       ;; TODO Add file input
       [:div {:class "flex items-center px-8 py-4 bg-gray-100 border-t border-gray-200"}
        (when (empty? (j/get user :deleted_at))
          [delete-button {:on-delete destroy}
           "Delete User"])
        [loading-button {:loading processing
                         :type "submit"
                         :class "ml-auto btn-indigo"}
         "Update User"]]]]]))

(defn create []
  [:f> create-form])

(defn edit [{:keys [user]}]
  [:f> edit-form user])
