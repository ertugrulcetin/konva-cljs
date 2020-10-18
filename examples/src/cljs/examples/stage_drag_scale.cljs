(ns examples.stage-drag-scale
  (:require [reagent.core :as r]
            [konva-cljs.core :as k]
            [konva-cljs.components :as c]))


(defn- limit-panning [v]
  (let [limit 1000]
    (if (> (js/Math.abs v) limit)
      (if (pos? v) limit (- limit))
      v)))


(defn- wheel [e state]
  (let [stage (.getStage (.-target e))
        e     (.-evt e)]
    (.preventDefault e)
    (if (.-ctrlKey e)
      (let [old-scale   (.scaleX stage)
            pointer     (.getPointerPosition stage)
            mouse-point {:x (- (/ (.-x pointer) old-scale)
                               (/ (.x stage) old-scale))
                         :y (- (/ (.-y pointer) old-scale)
                               (/ (.y stage) old-scale))}
            scale-by    1.05
            new-scale   (if (< (.-deltaY e) 0) (* old-scale scale-by) (/ old-scale scale-by))
            new-scale   (if (<= new-scale 0.15) 0.15 new-scale)]
        (swap! state assoc
               :scale new-scale
               :x (limit-panning
                   (* (- (- (:x mouse-point)
                            (/ (.-x pointer) new-scale))) new-scale))
               :y (limit-panning
                   (* (- (- (:y mouse-point)
                            (/ (.-y pointer) new-scale))) new-scale))))
      (do
        (swap! state update :x (fn [x] (limit-panning (- x (.-deltaX e)))))
        (swap! state update :y (fn [y] (limit-panning (- y (.-deltaY e)))))))
    (.batchDraw stage)))


(defn- on-drag [e state]
  (when (= "Stage" (.getClassName (.-target e)))
    (swap! state assoc
           :x (.x (.-target e))
           :y (.y (.-target e)))))


(defn stage-drag-scale-comp []
  (let [state (r/atom {:scale 1
                       :x     0
                       :y     0})]
    (r/create-class
     {:component-did-mount (fn [])
      :reagent-render      (fn []
                             [c/stage {:width         (.-innerWidth js/window)
                                       :height        (.-innerHeight js/window)
                                       :draggable     true
                                       :ref           #(swap! state assoc :stage %)
                                       :scale-x       (:scale @state)
                                       :scale-y       (:scale @state)
                                       :x             (:x @state)
                                       :y             (:y @state)
                                       :on-wheel      #(wheel % state)
                                       :on-drag-start #(on-drag % state)
                                       :on-drag-end   #(on-drag % state)}
                              [c/layer
                               [c/shape {:fill         "#00D2FF"
                                         :stroke       "black"
                                         :stroke-width 4
                                         :ref          #(swap! state assoc :shape %)
                                         :scene-func   (fn [ctx shape]
                                                         (-> ctx
                                                             (k/begin-path)
                                                             (k/move-to 20 50)
                                                             (k/line-to 220 80)
                                                             (k/quadratic-curve-to {:cpx 150 :cpy 100 :x 260 :y 170})
                                                             (k/close-path)
                                                             (k/fill-stroke-shape shape)))}]
                               [c/rect {:x         300
                                        :y         400
                                        :width     100
                                        :height    100
                                        :draggable true
                                        :fill      "red"
                                        :ref       #(swap! state assoc :rect %)}]]])})))
