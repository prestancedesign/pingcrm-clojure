(ns pingcrm.users.handlers
  (:require [inertia.middleware :as inertia]
            [pingcrm.db :as db]))

(defn get-users [{:keys [params]}]
  (let [filters (select-keys params [:search :role :trashed])
        props {:users (db/retrieve-and-filter-users filters)
               :filters filters}]
    (inertia/render "Users/Index" props)))

(defn edit-user [{:keys [path-params]}]
  (let [props {:user (db/get-user-by-id (:user-id path-params))}]
    (inertia/render "Users/Edit" props)))
