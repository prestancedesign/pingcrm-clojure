(ns pingcrm.pages.login
  (:require ["@inertiajs/inertia-react" :refer [useForm]]
            [applied-science.js-interop :as j]
            [pingcrm.shared.buttons :refer [loading-button]]
            [pingcrm.shared.form-input :refer [text-input]]
            [pingcrm.shared.logo :refer [logo]]
            [pingcrm.shared.site-head :refer [site-head]]))

(defn login-form []
  (let [{:keys [data setData errors post processing]} (j/lookup
                                                       (useForm #js {:email "johndoe@example.com"
                                                                     :password "secret"
                                                                     :remember false}))
        on-submit #(do (.preventDefault %)
                       (post "/login"))]
    [:<>
     [site-head {:title "Login"}]
     [:div {:class "p-6 bg-indigo-800 min-h-screen flex justify-center items-center"}
      [:div {:class "w-full max-w-md"}
       [logo {:class "block mx-auto w-full max-w-xs fill-white" :height "50"}]
       [:form {:on-submit on-submit
               :class "mt-8 overflow-hidden bg-white rounded-lg shadow-xl"}
        [:div {:class "px-10 py-12"}
         [:h1 {:class "text-3xl font-bold text-center"} "Welcome Back!"]
         [:div {:class "w-24 mx-auto mt-6 border-b-2"}]
         [text-input {:class "mt-10"
                      :label "Email"
                      :name "email"
                      :errors (.-email errors)
                      :value (.-email data)
                      :on-change #(setData "email" (.. % -target -value))}]
         [text-input {:class "mt-6"
                      :label "Password"
                      :name "password"
                      :type "password"
                      :errors (.-password errors)
                      :value (.-password data)
                      :on-change #(setData "password" (.. % -target -value))}]
         [:label {:class "flex items-center mt-6 select-none" :html-for "remember"}
          [:input#remember
           {:name "remember"
            :class "mr-1"
            :type "checkbox"
            :checked (.-remember data)
            :on-change #(setData "remember" (.. % -target -checked))}]
          [:span {:class "text-sm"} "Remember Me"]]]
        [:div {:class "px-10 py-4 bg-gray-100 border-t border-gray-100 flex"}
         [loading-button
          {:type "submit" :loading processing :class "btn-indigo  ml-auto"}
          "Login"]]]]]]))

(defn login []
  [:f> login-form])
