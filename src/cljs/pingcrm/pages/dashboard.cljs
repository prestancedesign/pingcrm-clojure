(ns pingcrm.pages.dashboard)

(defn index
  []
  [:div [:h1.mb-8.font-bold.text-3xl "Dashboard"]
   [:p.mb-8.leading-normal "Hey there! Welcome to the" [:b "Clojure"]
    "version of Ping CRM, a demo app designed to help illustrate how"
    [:a.text-indigo-500.underline.hover:text-orange-600
     {:href "https://inertiajs.com"} "Inertia.js"] "works."]
   [:p.mb-1.leading-normal "Source code of this demo available"
    [:a.text-indigo-500.underline.hover:text-orange-600
     {:href "https://github.com/prestancedesign/clojure-inertia-pingcrm-demo"}
     "here."]]
   [:p.mb-8.leading-normal "More information about the Clojure adapter"
    [:a.text-indigo-500.underline.hover:text-orange-600
     {:href "https://github.com/prestancedesign/inertia-clojure"} "here."]]])
