(ns pingcrm.ssr
  (:require ["@inertiajs/inertia-react" :refer [App createInertiaApp]]
            ["express" :as express]
            [applied-science.js-interop :as j]
            [pingcrm.shared-pages :refer [pages]]
            [pingcrm.shared.layout :refer [layout]]
            [reagent.core :as r]
            [reagent.dom.server :as ssr]))

(defn init []
  (let [port (or (.. js/process -env -PORT) 8000)]
    (-> (express)
        (.use (.json express))
        (.post "/render"
               (fn [req res next]
                 (try
                   (-> (createInertiaApp
                        #js {:resolve (fn [name]
                                        (let [^js comp (r/reactify-component (get pages name))]
                                          (when-not (= name "Auth/Login")
                                            (set! (.-layout comp) (fn [page] (r/as-element [layout page]))))
                                          comp))
                             :page (.-body req)
                             :render ssr/render-to-string
                             :setup (j/fn [^:js {:keys [App props]}]
                                      (r/as-element [:f> App props]))})
                       (.then (fn [page] (.json res page))))
                   (catch js/Error error (next error)))))
        (.listen port #(js/console.log "Server started.")))))
