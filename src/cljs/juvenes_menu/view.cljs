(ns juvenes-menu.view
  (:require [dommy.utils :as utils]
            [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel1]]))

(defn list-template [kitchen-name]
  (node [:ul {:class kitchen-name}]))

(defn menuitem-template [menu-item]
  (node [:li {:class "menu-item"} (:main-dish menu-item)]))

(defn restaurant-template [kitchen-name]
  (node [:div {:class "restaurant-info" :id kitchen-name}
         	[:h2 {:class "restaurant-name"} kitchen-name]]))

(defn render-menu [data]
  (let [kitchen-name (:kitchen-name data)]
  	(-> (sel1 ".menu-container")
        (dommy/append! (restaurant-template kitchen-name)))
    (doseq [menu-item (:kitchen-menu data)]
      (-> (sel1 (str "#" kitchen-name))
          (dommy/append! (menuitem-template menu-item))))))