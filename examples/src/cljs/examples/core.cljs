(ns examples.core
  (:require [reagent.dom :as rdom]
            [goog.dom :as dom]
            [examples.rect-with-transformer :refer [rect-with-transformer-comp]]
            [examples.custom-shape :refer [custom-shape-comp]]
            [examples.stars :refer [stars-comp]]
            [examples.draggable-text :refer [draggable-text-comp]]
            [examples.simple-animations :refer [simple-animation-comp]]
            [examples.blurred-rect :refer [blurred-rect-comp]]
            [examples.canvas-scrolling :refer [canvas-scroll-comp]]))


;; REPLACE COMPONENTS MANUALLY...
(defn main-panel []
  canvas-scroll-comp)


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (mount-root))
