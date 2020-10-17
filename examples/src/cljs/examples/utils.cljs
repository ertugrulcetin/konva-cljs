(ns examples.utils
  (:require
   [goog.string :as gstring]
   [goog.string.format]))


(defn format [fmt & args]
  (apply gstring/format (cons fmt args)))
