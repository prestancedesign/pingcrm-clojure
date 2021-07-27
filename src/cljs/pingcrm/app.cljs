(ns pingcrm.app
  (:require ["@inertiajs/inertia-react" :refer [createInertiaApp]]
            ["@inertiajs/progress" :refer [InertiaProgress]]
            [reagent.core :as r]
            [reagent.dom :as d]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.shared.layout :refer [layout]]
            [pingcrm.pages.login :refer [login]]
            [pingcrm.pages.organizations :as organizations]
            [pingcrm.pages.contacts :as contacts]
            [pingcrm.pages.reports :as reports]
            [pingcrm.pages.users :as users]
            [applied-science.js-interop :as j]))

(.init InertiaProgress)

(def el (.getElementById js/document "app"))

(def pages {"Dashboard/Index" dashboard/index
            "Auth/Login" login
            "Reports/Index" reports/index
            "Organizations/Index" organizations/index
            "Organizations/Create" organizations/create
            "Organizations/Edit" organizations/edit
            "Contacts/Index" contacts/index
            "Contacts/Create" contacts/create
            "Contacts/Edit" contacts/edit
            "Users/Index" users/index
            "Users/Create" users/create
            "Users/Edit" users/edit})

(defn start []
  (createInertiaApp
   #js {:resolve (fn [name]
                   (let [^js comp (r/reactify-component (get pages name))]
                     (set! (.-layout comp) (fn [page] (r/as-element [layout page])))
                     comp))
        :title (fn [title] (str title " | Ping CRM"))
        :setup (j/fn [^:js {:keys [el App props]}]
                 (d/render (r/as-element [:f> App props]) el))}))

(defn init! []
  (start))
