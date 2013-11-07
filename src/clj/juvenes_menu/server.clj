(ns juvenes-menu.server
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [resources not-found]]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :as resources]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.util.response :refer [redirect]])
  (:gen-class))

(defroutes app-routes
  (GET "/" [] (redirect "/menu.html"))
  (resources "/")
  (not-found "Page not found"))

(def handler
  (site app-routes))

(def app
  (-> handler
      (wrap-gzip)))

(defn -main [& args]
  (let [port (Integer/parseInt (get (System/getenv) "PORT" "8080"))]
    (run-jetty app {:port port})))

