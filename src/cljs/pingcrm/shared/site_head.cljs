(ns pingcrm.shared.site-head
  (:require ["@inertiajs/inertia-react" :refer [InertiaHead]]))

(defn site-head [{:keys [title]}]
  [:> InertiaHead {:title (str title " | Ping CRM")}])
