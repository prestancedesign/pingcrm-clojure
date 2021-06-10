(ns pingcrm.handlers.reports
  (:require [inertia.middleware :as inertia]))

(defn index [_]
  (inertia/render "Reports/Index"))
