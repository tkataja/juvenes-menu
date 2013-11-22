(defproject juvenes-menu "0.1.0-SNAPSHOT"
  :description "Juvenes TTY menus"
  :url "http://github.com/tkataja/juvenes-menu"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-1978"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [bk/ring-gzip "0.1.1"]
                 [compojure "1.1.5"]
                 [prismatic/dommy "0.1.1"]
                 [ring "1.1.8"]
                 [shoreleave/shoreleave-remote "0.3.0"]]
  :plugins [[lein-cljsbuild "0.3.4"]
            [lein-ring "0.8.7"]]
  :uberjar-name "juvenes-menu-standalone.jar"
  :min-lein-version "2.0.0"
  :source-paths ["src/clj"]
  :cljsbuild {:builds
              {:prod
               {
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/juvenes-menu.js"
                           :optimizations :advanced
                           :pretty-print false}}}}
  :main juvenes-menu.server
  :ring {:handler juvenes-menu.server/app})

