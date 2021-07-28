(ns pingcrm.app
  (:require ["@inertiajs/inertia" :refer [Inertia]]
            ["@inertiajs/inertia-react" :refer [createInertiaApp]]
            ["@inertiajs/progress" :refer [InertiaProgress]]
            [applied-science.js-interop :as j]
            [pingcrm.pages.contacts :as contacts]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.pages.login :refer [login]]
            [pingcrm.pages.organizations :as organizations]
            [pingcrm.pages.reports :as reports]
            [pingcrm.pages.users :as users]
            [pingcrm.shared.layout :refer [layout]]
            [reagent.core :as r]
            [reagent.dom :as d]))

(.init InertiaProgress)

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

(defn init! []
  (createInertiaApp
   #js {:resolve (fn [name]
                   (let [^js comp (r/reactify-component (get pages name))]
                     (when-not (= name "Auth/Login")
                       (set! (.-layout comp) (fn [page] (r/as-element [layout page]))))
                     comp))
        :title (fn [title] (str title " | Ping CRM"))
        :setup (j/fn [^:js {:keys [el App props]}]
                 (d/render (r/as-element [:f> App props]) el))}))

(defn ^:dev/after-load reload []
  (.reload Inertia))
