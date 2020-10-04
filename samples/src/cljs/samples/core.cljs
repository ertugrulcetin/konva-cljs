(ns samples.core
  (:require [reagent.dom :as rdom]
            [goog.dom :as dom]
            [samples.rect-with-transformer :refer [rect-with-transformer-comp]]
            [samples.custom-shape :refer [custom-shape-comp]]
            [samples.stars :refer [stars-comp]]
            [samples.draggable-text :refer [draggable-text-comp]]))


;; Replace components manually...
(defn main-panel []
  stars-comp)


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (mount-root))
