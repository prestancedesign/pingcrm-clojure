(ns pingcrm.shared.pagination)

(defn pagination-links [current-page total per-page]
  (let [url "http://localhost:3000/organizations?page="
        page-number (/ total per-page)
        previous-link {:url (when (> current-page 1) (str url (dec current-page))) :label "Previous" :active (when (> current-page 1) true)}
        next-link {:url (when (< current-page page-number) (str url (inc current-page))) :label "Next" :active (when (<= current-page (dec page-number)) true)}
        links (for [item (range 1 (inc page-number))]
                {:url (str url item) :label (str item) :active (when (= item current-page) true)})]
    (concat [previous-link] links [next-link])))
