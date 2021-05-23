(ns dev.src.user
  (:require [integrant.repl :as ig-repl]
            [integrant.repl.state :as state]
            [pingcrm.models.organizations :as org]
            [pingcrm.models.users :as users]
            [pingcrm.system :as system]))

(ig-repl/set-prep!
 (fn [] system/config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :pingcrm/app))
(def db (-> state/system :database.sql/connection))

(comment
  (org/retrieve-and-filter-organization db nil 0)

  (org/count-organizations db)

  (users/retrieve-and-filter-users nil))
