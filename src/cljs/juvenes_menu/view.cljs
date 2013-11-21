(ns juvenes-menu.view
  (:require [dommy.utils :as utils]
            [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel1]]))

(defn main-dish-template [kitchen-menu]
  (let [main-dish (:main-dish kitchen-menu)
        side-dishes (:side-dishes kitchen-menu)]
    (node 
      [:ul {:class "main-dishes-group"}
       [:li {:class "main-dish-item"} main-dish
        [:ul {:class "side-dishes-group"}
         (for [side-dish side-dishes]
           [:li {:class "side-dish-item"} side-dish])]]])))

(defn kitchen-template [kitchen-name]
  (node [:div {:class "kitchen-info" :id kitchen-name}
         [:h2 kitchen-name]]))

(defn render-menu [data]
  (let [kitchen-name (:kitchen-name data)]
    (-> (sel1 ".menu-container")
        (dommy/append! (kitchen-template kitchen-name)))
    (doseq [menu-item (:kitchen-menu data)]
      (-> (sel1 (str "#" kitchen-name))
          (dommy/append! (main-dish-template menu-item))))))