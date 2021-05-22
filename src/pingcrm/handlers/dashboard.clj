(ns pingcrm.handlers.dashboard
  (:require [inertia.middleware :as inertia]))

(defn index [_]
  (inertia/render "Dashboard/Index"))
