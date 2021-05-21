(ns dev.src.user
  (:require [pingcrm.system :as system]
            [integrant.repl.state :as state]
            [integrant.repl :as ig-repl]))

(ig-repl/set-prep!
 (fn [] system/config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :pingcrm/app))
(def db (-> state/system :database.sql/connection))
