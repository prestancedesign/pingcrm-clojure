(ns pingcrm.shared.flash-messages
  (:require ["@inertiajs/inertia-react" :refer [usePage]]
            [applied-science.js-interop :as j]
            ["react" :as react]))

(defn icon-success []
  [:svg {:class "ml-4 mr-2 flex-shrink-0 w-4 h-4 text-white fill-current"
         :xmlns "http://www.w3.org/2000/svg"
         :view-box "0 0 20 20"}
   [:polygon {:points "0 11 2 9 7 14 18 3 20 5 7 18"}]])

(defn icon-danger []
  [:svg {:class "ml-4 mr-2 flex-shrink-0 w-4 h-4 text-white fill-current"
         :xmlns "http://www.w3.org/2000/svg"
         :view-box "0 0 20 20"}
   [:path {:d "M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm1.41-1.41A8 8 0 1 0 15.66 4.34 8 8 0 0 0 4.34 15.66zm9.9-8.49L11.41 10l2.83 2.83-1.41 1.41L10 11.41l-2.83 2.83-1.41-1.41L8.59 10 5.76 7.17l1.41-1.41L10 8.59l2.83-2.83 1.41 1.41z"}]])

(defn button-close [{:keys [color on-click]}]
  (let [class-name (condp = color
                     "red" "text-red-700 group-hover:text-red-800"
                     "green" "text-green-700 group-hover:text-green-800")]
    [:button {:on-click on-click
              :type "button"
              :class "focus:outline-none group mr-2 p-2"}
     [:svg {:class (str "block  w-2 h-2 fill-current " class-name)
            :xmlns "http://www.w3.org/2000/svg"
            :width "235.908"
            :height "235.908"
            :view-box "278.046 126.846 235.908 235.908"}
      [:path {:d "M506.784 134.017c-9.56-9.56-25.06-9.56-34.62 0L396 210.18l-76.164-76.164c-9.56-9.56-25.06-9.56-34.62 0-9.56 9.56-9.56 25.06 0 34.62L361.38 244.8l-76.164 76.165c-9.56 9.56-9.56 25.06 0 34.62 9.56 9.56 25.06 9.56 34.62 0L396 279.42l76.164 76.165c9.56 9.56 25.06 9.56 34.62 0 9.56-9.56 9.56-25.06 0-34.62L430.62 244.8l76.164-76.163c9.56-9.56 9.56-25.06 0-34.62z"}]]]))

(defn flash-messages []
  (let [{:keys [flash errors]} (j/lookup (.-props (usePage)))
        [visible setVisible] (react/useState true)
        num-of-errors (count (js->clj errors))]

    (react/useEffect
     (fn [] (setVisible true))
     #js [flash errors]) ; TODO Check if parameters is working

    (cond
      (and (not-empty (.-success flash)) visible)
      [:div {:class "mb-8 flex items-center justify-between bg-green-500 rounded max-w-3xl"}
       [:div {:class "flex items-center"}
        [icon-success]
        [:div {:class "py-4 text-white text-sm font-medium"}
         (.-success flash)]]
       [button-close {:on-click #(setVisible false)
                      :color "green"}]]

      (and (> num-of-errors 0) visible)
      [:div {:class "mb-8 flex items-center justify-between bg-red-400 rounded max-w-3xl"}
       [:div {:class "flex items-center"}
        [icon-danger]
        [:div {:class "py-4 text-white text-sm font-medium"}
         (cond
           (= num-of-errors 1) "There is one form error"
           (> num-of-errors 1) (str "There are " num-of-errors " form errors."))]]
       [button-close {:on-click #(setVisible false)
                      :color "red"}]])))
