(ns konva-cljs.views
  (:require [konva-cljs.nodes :as n]
            [reagent.core :as r]))


(defn begin-path [ctx]
  (doto ctx .beginPath))


(defn close-path [ctx]
  (doto ctx .closePath))


(defn quadratic-curve-to
  [ctx {:keys [cpx cpy x y]}]
  (doto ctx (.quadraticCurveTo cpx cpy x y)))


(defn move-to
  [ctx x y]
  (doto ctx (.moveTo x y)))


(defn line-to
  [ctx x y]
  (doto ctx (.lineTo x y)))


(defn fill-stroke-shape [ctx shape]
  (doto ctx (.fillStrokeShape shape)))


(defn main-panel []
  )
