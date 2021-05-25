# Ping CRM on Clojure

A demo application built with Clojure to illustrate how Inertia.js and works.

![](screenshot.png)

This is a port of the original Ping CRM written in Laravel/PHP to Clojure with Integrant, Ring, Reitit and next.jdbc.
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

# License & Copyright

Copyright (c) 2021 Prestance / Michaël SALIHI.
Distributed under the MIT license.
