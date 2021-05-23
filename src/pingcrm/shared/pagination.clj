(ns pingcrm.shared.pagination)

(defn pagination-links [uri current-page total per-page]
  (let [uri (str uri "?page=")
        page-number (/ total per-page)
        previous-link {:url (when (> current-page 1) (str uri (dec current-page))) :label "Previous" :active (when (> current-page 1) true)}
        next-link {:url (when (< current-page page-number) (str uri (inc current-page))) :label "Next" :active (when (<= current-page (dec page-number)) true)}
        links (for [item (range 1 (inc page-number))]
                {:url (str uri item) :label (str item) :active (when (= item current-page) true)})]
    (concat [previous-link] links [next-link])))
