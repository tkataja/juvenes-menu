(ns juvenes-menu.util
  (:require [clojure.string :as str]
    		[goog.date :as date]))

(defn json-parse
  "Parses a JSON string to JS object which is transformed to Clojure map."
  [string]
  (js->clj (js/JSON.parse string)))

(defn weekday []
  (.getDay (date/Date.)))

(defn year []
  (.getFullYear (date/Date.)))

(defn week-number []
  (.getWeekNumber (date/Date.)))

