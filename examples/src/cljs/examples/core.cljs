(ns examples.core
  (:require [reagent.dom :as rdom]
            [goog.dom :as dom]
            [examples.rect-with-transformer :refer [rect-with-transformer-comp]]
            [examples.custom-shape :refer [custom-shape-comp]]
            [examples.stars :refer [stars-comp]]
            [examples.draggable-text :refer [draggable-text-comp]]))


;; REPLACE COMPONENTS MANUALLY...
(defn main-panel []
  stars-comp)


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (mount-root))
