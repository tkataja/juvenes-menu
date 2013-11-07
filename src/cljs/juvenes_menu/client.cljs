(ns juvenes-menu.client
  (:require [clojure.string :as str]
            [dommy.utils :as utils]
            [dommy.core :as dommy]
            [shoreleave.remotes.jsonp :refer [jsonp]]
            [juvenes-menu.util :refer [json-parse week-number weekday]])
  (:use-macros
            [dommy.macros :only [node sel1]]))

(def juvenes-ids {:zip {:kitchen-id 12 :menutype-id 60}
                  :edison {:kitchen-id 2 :menutype-id 60}
                  :newton {:kitchen-id 6 :menutype-id 60}
                  :fusion {:kitchen-id 6 :menutype-id 3}})

(def menu-url "http://www.juvenes.fi/DesktopModules/Talents.LunchMenu/LunchMenuServices.asmx/GetMenuByWeekday")

(defn url-parameter [map-entry]
  (str (key map-entry) "=" (val map-entry)))

(defn query-string [query-params]
  (str "?" (str/join "&" (map url-parameter query-params))))

(defn meal-options [kitchen-json]
  (get kitchen-json "MealOptions"))

(defn menu-items [meal-options]
  (map #(first (get % "MenuItems")) meal-options))

(defn menu-names [menu-items]
  (map #(get % "Name") menu-items))

(defn handle-data [data]
  (-> (get data :d)
      (json-parse)
      (meal-options)
      (menu-items)
      (menu-names)))

(defn kitchen-id [kitchen]
  (get-in juvenes-ids [kitchen :kitchen-id]))

(defn menutype-id [kitchen]
  (get-in juvenes-ids [kitchen :menutype-id]))

(defn menuitem-template [menu-item]
  (node [:li {:class "menu-item"} menu-item]))

(defn output-menu [data]
  (doseq [menu-item (handle-data data)]
    (-> (sel1 ".menu-list")
        (dommy/append! (menuitem-template menu-item)))))

(defn menu-today [kitchen]
  (let [query-params {"KitchenId" (get-in juvenes-ids [kitchen :kitchen-id])
                      "MenuTypeId" (get-in juvenes-ids [kitchen :menutype-id])
                      "Week" (week-number)
                      "WeekDay" (weekday)
                      "lang" "'fi'"
                      "format" "json"}
        query (query-string query-params)
        request-url (str menu-url query)]
    (jsonp request-url :on-success #(output-menu %))))

(defn ^:export init []
  (menu-today :zip))
