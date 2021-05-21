((clojure-mode
     (cider-ns-refresh-before-fn . "integrant.repl/suspend")
     (cider-ns-refresh-after-fn . "integrant.repl/resume")
     (cider-clojure-cli-global-options . "-A:dev")))
