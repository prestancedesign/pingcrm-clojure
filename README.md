# Ping CRM on Clojure

A demo application built with Clojure to illustrate how [Inertia.js](https://inertiajs.com/) works.

![](screenshot.png)

This is a port of the original [Ping CRM](https://github.com/inertiajs/pingcrm) written in Laravel/PHP to Clojure with Integrant, Ring, Reitit and next.jdbc.
For now the Vue.js front-end come from the original demo.

I am planning a ClojureScript Reagent version later.

## Usage

Clone the repo locally:

    git clone https://github.com/prestancedesign/clojure-inertia-pingcrm-demo
    cd clojure-inertia-pingcrm-demo

### Run the application ###

    clj -M:run

You're ready to go! Open [http://localhost:3000](http://localhost:3000) and login with:

- **Username:** johndoe@example.com
- **Password:** secret


### Run the application in REPL

    clj -M:dev

Once REPL starts, run the system:

    (go)

Open [http://localhost:3000](http://localhost:3000)

## External resources

- Clojure Inertia adapter: https://github.com/prestancedesign/inertia-clojure
- Inertia.js documentation: https://inertiajs.com/
- Original Ping CRM demo written in PHP Laravel/Vue.js: https://github.com/inertiajs/pingcrm

## License & Copyright

Copyright (c) 2021 Prestance / Michaël SALIHI.
Distributed under the MIT license.
