(defproject juvenes-menu "0.1.0-SNAPSHOT"
  :description "Juvenes TTY menus"
  :url "http://github.com/tkataja/juvenes-menu"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1978"]
                 [bk/ring-gzip "0.1.1"]
                 [compojure "1.1.5"]
                 [prismatic/dommy "0.1.1"]
                 [ring "1.1.8"]
                 [shoreleave/shoreleave-remote "0.3.0"]]
  :plugins [[lein-cljsbuild "0.3.4"]
            [lein-ring "0.8.7"]]
  ;:hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]
  :cljsbuild {:builds
              {:dbg
               {;; clojurescript source code path
                :source-paths ["src/brepl" "src/cljs"]

                ;; Google Closure Compiler options
                :compiler {;; the name of emitted JS script file
                           :output-to "resources/public/js/juvenes-menu-dbg.js"

                           ;; minimum optimization
                           :optimizations :whitespace

                           ;; prettyfying emitted JS
                           :pretty-print true}}
               :pre
               {;; clojurescript source code path
                :source-paths ["src/brepl" "src/cljs"]
                :compiler {;; different output name
                           :output-to "resources/public/js/juvenes-menu-pre.js"

                           ;; simple optmization
                           :optimizations :simple

                           ;; no need prettyfication
                           :pretty-print false}}

               :prod
               {;; clojurescript source code path
                :source-paths ["src/cljs"]

                ;; Google Closure Compiler options
                :compiler {;; the name of emitted JS script file
                           :output-to "resources/public/js/juvenes-menu.js"

                           ;; advanced optimization
                           :optimizations :advanced

                           ;; no need prettyfication
                           :pretty-print false}}}}
  :main juvenes-menu.server
  :ring {:handler juvenes-menu.server/app})

