(ns juvenes-menu.data
  (:require [clojure.string :as str]
            [cljs.core.async :refer [put! chan <!]]
            [juvenes-menu.util :refer [json-parse week-number weekday]]
            [shoreleave.remotes.jsonp :refer [jsonp]]))

(def menu-url "http://www.juvenes.fi/DesktopModules/Talents.LunchMenu/LunchMenuServices.asmx/GetMenuByWeekday")

(def juvenes-ids {:zip {:kitchen-id 12 :menutype-id 60}
                  :edison {:kitchen-id 2 :menutype-id 60}
                  :newton {:kitchen-id 6 :menutype-id 60}
                  :fusion {:kitchen-id 6 :menutype-id 3}})

(defn url-parameter [map-entry]
  (str (key map-entry) "=" (val map-entry)))

(defn create-query-string [query-params]
  (str "?" (str/join "&" (map url-parameter query-params))))

(defn request-url-today [kitchen]
  (let [query-params {"KitchenId" (get-in juvenes-ids [kitchen :kitchen-id])
                      "MenuTypeId" (get-in juvenes-ids [kitchen :menutype-id])
                      "Week" (week-number)
                      "WeekDay" (weekday)
                      "lang" "'fi'"
                      "format" "json"}]
    (str menu-url (create-query-string query-params))))

(defn get-kitchens []
  (keys juvenes-ids))

(defn meal-options [menu-json]
  (get menu-json "MealOptions"))

(defn menu-items [meal-options]
  (map #(get % "MenuItems") meal-options))

(defn menu-names [menu-items]
  (map (fn [item]
         (map #(get % "Name") item)) menu-items))

(defn categorize [menu-names]
  (map #(hash-map :main-dish (first %) :side-dishes (rest %)) menu-names))

;; The menu data will be in the following format:
;; {:kitchen-name <name of the kitchen>
;;  :kitchen-menu {:main-dish <main dish name> :side-dishes [<side dish 1>, <side dish 2>, ...]}}

(defn create-menu [data]
  (-> (:d data)      ; Get the kitchen menu as JSON string
      (json-parse)   ; Convert JSON string->JS object->CLJS data structure
      (meal-options) 
      (menu-items)   
      (menu-names)   
      (categorize))) ; Separate to main dish and side dishes

;; Fetch the menu and put the result on channel like in 
;; the following tutorial.
;; http://swannodette.github.io/2013/11/07/clojurescript-101/

(defn menu-today [kitchen]
  (let [channel (chan)]
    (jsonp (request-url-today kitchen)
           :on-success 
           (fn [data]
             (let [kitchen-name (str/capitalize (name kitchen))
                   kitchen-menu (create-menu data)]
               (put! channel (hash-map :kitchen-name kitchen-name
                                       :kitchen-menu kitchen-menu))))
           :on-timeout 
           (fn [_]
             (js/console.log "Couldn't fetch the menu")))
    channel))