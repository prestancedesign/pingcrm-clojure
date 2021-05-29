(ns pingcrm.app
  (:require ["@inertiajs/inertia-react" :refer [App]]
            [reagent.core :as r]
            [reagent.dom :as d]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.shared.layout :refer [layout]]))

(def el (.getElementById js/document "app"))

(def pages {"Dashboard/Index" dashboard/index})

(defn app []
  [:> App {:initial-page (.parse js/JSON (.. el -dataset -page))
           :resolve-component (fn [name] (let [comp (r/reactify-component (get pages name))]
                                           (set! (.-layout ^js comp) (fn [page] (r/as-element [layout page])))
                                           comp))}])

(defn mount-root []
  (d/render [app] el))

(defn init! []
  (mount-root))
