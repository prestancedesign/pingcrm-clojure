(ns pingcrm.app
  (:require ["@inertiajs/inertia-react" :refer [createInertiaApp]]
            ["@inertiajs/progress" :refer [InertiaProgress]]
            [applied-science.js-interop :as j]
            [pingcrm.shared-pages :refer [pages]]
            [pingcrm.shared.layout :refer [layout]]
            [reagent.core :as r]
            [reagent.dom :as d]))

(.init InertiaProgress)

(defn start []
  (createInertiaApp
   #js {:resolve (fn [name]
                   (let [^js comp (r/reactify-component (get pages name))]
                     (when-not (= name "Auth/Login")
                       (set! (.-layout comp) (fn [page] (r/as-element [layout page]))))
                     comp))
        :title (fn [title] (str title " | Ping CRM"))
        :setup (j/fn [^:js {:keys [el App props]}]
                 (d/render (r/as-element [:f> App props]) el))}))

(defn init! []
  (start))
