(ns pingcrm.app
  (:require ["@inertiajs/inertia-react" :refer [App]]
            ["@inertiajs/progress" :refer [InertiaProgress]]
            [reagent.core :as r]
            [reagent.dom :as d]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.shared.layout :refer [layout]]
            [pingcrm.pages.login :refer [login]]
            [pingcrm.pages.organizations :as organizations]))

(.init InertiaProgress)

(def el (.getElementById js/document "app"))

(def pages {"Dashboard/Index" dashboard/index
            "Auth/Login" login
            "Organizations/Index" organizations/index
            "Organizations/Create" organizations/create
            "Organizations/Edit" organizations/edit})

(defn app []
  [:> App {:initial-page (.parse js/JSON (.. el -dataset -page))
           :resolve-component (fn [name] (let [comp (r/reactify-component (get pages name))]
                                          (when-not (= name "Auth/Login")
                                            (set! (.-layout ^js comp) (fn [page] (r/as-element [layout page]))))
                                          comp))}])

(defn mount-root []
  (d/render [app] el))

(defn init! []
  (mount-root))
