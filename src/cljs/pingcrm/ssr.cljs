(ns pingcrm.ssr
  (:require ["@inertiajs/inertia-react" :refer [App createInertiaApp]]
            ["express" :as express]
            [applied-science.js-interop :as j]
            [pingcrm.pages.contacts :as contacts]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.pages.login :refer [login]]
            [pingcrm.pages.organizations :as organizations]
            [pingcrm.pages.reports :as reports]
            [pingcrm.pages.users :as users]
            [pingcrm.shared.layout :refer [layout]]
            [reagent.core :as r]
            [reagent.dom.server :as ssr]))

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

(defn init []
  (let [port (or (.. js/process -env -PORT) 8000)]
    (-> (express)
        (.use (.json express))
        (.post "/render" (fn [req res next]
                           (try
                             (-> (createInertiaApp
                                  (clj->js {:resolve (fn [name]
                                                       (let [^js comp (r/reactify-component (get pages name))]
                                                         (set! (.-layout comp) (fn [page] (r/as-element [layout page])))
                                                         comp))
                                            :page (.-body req)
                                            :render ssr/render-to-string
                                            :setup (j/fn [^:js {:keys [App props]}]
                                                     (r/as-element [:f> App props]))}))
                                 (.then (fn [page] (.json res page))))
                             (catch js/Error error (next error)))))
        (.listen port))))
