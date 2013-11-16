(ns juvenes-menu.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [juvenes-menu.data :as data]
            [juvenes-menu.view :as view]
            [cljs.core.async :refer [<!]]))

(defn ^:export init []
  (doseq [restaurant (data/get-restaurants)]
    (go (view/output-menu (<! (data/menu-today restaurant))))))
