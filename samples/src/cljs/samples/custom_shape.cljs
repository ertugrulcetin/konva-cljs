(ns samples.custom-shape
  (:require [konva-cljs.core :as k]
            [konva-cljs.components :as c]))


(defn custom-shape-comp []
  [c/stage {:width  (.-innerWidth js/window)
            :height (.-innerHeight js/window)}
   [c/layer
    [c/shape {:fill         "#00D2FF"
              :stroke       "black"
              :stroke-width 4
              :scene-func   (fn [ctx shape]
                              (-> ctx
                                  (k/begin-path)
                                  (k/move-to 20 50)
                                  (k/line-to 220 80)
                                  (k/quadratic-curve-to {:cpx 150 :cpy 100 :x 260 :y 170})
                                  (k/close-path)
                                  (k/fill-stroke-shape shape)))}]]])
