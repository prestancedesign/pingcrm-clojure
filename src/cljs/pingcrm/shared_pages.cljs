(ns pingcrm.shared-pages
  (:require [pingcrm.pages.contacts :as contacts]
            [pingcrm.pages.dashboard :as dashboard]
            [pingcrm.pages.login :refer [login]]
            [pingcrm.pages.organizations :as organizations]
            [pingcrm.pages.reports :as reports]
            [pingcrm.pages.users :as users]))

(def pages {"Dashboard/Index" #'dashboard/index
            "Auth/Login" #'login
            "Reports/Index" #'reports/index
            "Organizations/Index" #'organizations/index
            "Organizations/Create" #'organizations/create
            "Organizations/Edit" #'organizations/edit
            "Contacts/Index" #'contacts/index
            "Contacts/Create" #'contacts/create
            "Contacts/Edit" #'contacts/edit
            "Users/Index" #'users/index
            "Users/Create" #'users/create
            "Users/Edit" #'users/edit})
