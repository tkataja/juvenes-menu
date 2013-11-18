(ns juvenes-menu.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [juvenes-menu.data :as data]
            [juvenes-menu.view :as view]
            [cljs.core.async :refer [<!]]))

(defn ^:export init []
  (doseq [kitchen (data/get-kitchens)]
    (go (view/render-menu (<! (data/menu-today kitchen))))))
