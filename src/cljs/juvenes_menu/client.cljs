(ns juvenes-menu.client
  (:require [juvenes-menu.data :as data]))

(defn ^:export init []
  (doseq [restaurant (data/get-restaurants)] (data/menu-today restaurant)))
