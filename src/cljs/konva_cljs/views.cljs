(ns konva-cljs.views
  (:require [konva-cljs.components :as kc]
            [konva-cljs.core :as ko]
            [reagent.core :as r]
            [goog.dom :as dom]
            [goog.object :as ob]
            [reagent.dom :as rdom]
            [clojure.string :as str]
            ["konva" :default k]))


(defn main-panel []
  )


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (println "ertu")
  (mount-root))
