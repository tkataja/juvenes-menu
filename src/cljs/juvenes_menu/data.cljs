(ns juvenes-menu.data
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.string :as str]
            [cljs.core.async :refer [put! chan <!]]
            [juvenes-menu.util :refer [json-parse week-number weekday]]
            [juvenes-menu.view :refer [output-menu]]
            [shoreleave.remotes.jsonp :refer [jsonp]]))

(def juvenes-ids {:zip {:kitchen-id 12 :menutype-id 60}
                  :edison {:kitchen-id 2 :menutype-id 60}
                  :newton {:kitchen-id 6 :menutype-id 60}
                  :fusion {:kitchen-id 6 :menutype-id 3}})

(defn url-parameter [map-entry]
  (str (key map-entry) "=" (val map-entry)))

(defn query-string [query-params]
  (str "?" (str/join "&" (map url-parameter query-params))))

(def menu-url "http://www.juvenes.fi/DesktopModules/Talents.LunchMenu/LunchMenuServices.asmx/GetMenuByWeekday")

(defn get-restaurants []
  (keys juvenes-ids))

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

;; Fetch the menu and put the result on channel like in 
;; the following tutorial.
;; http://swannodette.github.io/2013/11/07/clojurescript-101/

(defn menu-today [kitchen]
  (let [query-params {"KitchenId" (get-in juvenes-ids [kitchen :kitchen-id])
                      "MenuTypeId" (get-in juvenes-ids [kitchen :menutype-id])
                      "Week" (week-number)
                      "WeekDay" (weekday)
                      "lang" "'fi'"
                      "format" "json"}
        query (query-string query-params)
        request-url (str menu-url query)
        out (chan)]
    (jsonp request-url
           :on-success
           (fn [res]
             (put! out (into {} {:kitchen (str/capitalize (name kitchen))
                                 :menu (handle-data res)})))
           :on-timeout
           (fn [_]
             (js/console.log "Couldn't fetch the menu")))
    out))