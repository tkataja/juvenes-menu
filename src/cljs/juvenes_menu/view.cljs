(ns juvenes-menu.view
  (:require [dommy.utils :as utils]
            [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel1]]))

(defn list-template [kitchen]
  (node [:ul {:class (name kitchen)}]))

(defn menuitem-template [menu-item]
  (node [:li {:class "menu-item"} menu-item]))

(defn output-menu [kitchen data]
  (doseq [menu-item data]
    (-> (sel1 ".container-data")
        (dommy/append! (list-template kitchen)))
    (-> (sel1 (str "." (name kitchen)))
        (dommy/append! (menuitem-template menu-item)))))