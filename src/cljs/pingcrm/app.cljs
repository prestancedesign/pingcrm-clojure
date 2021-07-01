(ns pingcrm.app
  (:require ["@inertiajs/inertia-react" :refer [App]]
            ["@inertiajs/progress" :refer [InertiaProgress]]
            [pingcrm.shared-pages :refer [pages]]
            [pingcrm.shared.layout :refer [layout]]
            [reagent.core :as r]
            [reagent.dom :as d]))

(.init InertiaProgress)

(def el (.getElementById js/document "app"))

(defn app []
  [:> App
   {:initial-page (.parse js/JSON (.. el -dataset -page))
    :resolve-component
    (fn [name] (let [^js comp (r/reactify-component (get pages name))]
                (when-not (= name "Auth/Login")
                  (set! (.-layout comp) (fn [page] (r/as-element [layout page]))))
                comp))}])

(defn mount-root []
  (d/render [app] el))

(defn init! []
  (mount-root))
