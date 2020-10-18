(ns examples.stage-drag-scale
  (:require [reagent.core :as r]
            [konva-cljs.core :as k]
            [konva-cljs.components :as c]
            [goog.dom :as dom]))

(def rect-width 128)
(def rect-height 128)


(def img-p
  (let [data    ""
        dom-url (or (.-URL js/window)
                    (.-webkitURL js/window)
                    js/window)
        img     (js/Image.)
        svg     (js/Blob. (clj->js [data]) (clj->js {:type "image/svg+xml;charset=utf-8"}))
        url     (.createObjectURL dom-url svg)]
    (js/Promise. (fn [resolve]
                   (set! (.-onload img) #(resolve img))
                   (set! (.-src img) url)))))


(defn draw-board [state]
  (let [scale       (:scale @state)
        x           (:x @state)
        y           (:y @state)
        rect-width  (/ rect-width scale)
        rect-height (/ rect-height scale)
        width       (/ (.-innerWidth js/window) scale)
        height      (/ (.-innerHeight js/window) scale)
        start-x     (* (js/Math.floor (/ (- (- x) width) rect-width)) rect-width)
        end-x       (* (js/Math.floor (/ (+ (- x) (* width 2)) rect-width)) rect-width)
        start-y     (* (js/Math.floor (/ (- (- y) height) rect-height)) rect-height)
        end-y       (* (js/Math.floor (/ (+ (- y) (* height 2)) rect-height)) rect-height)]
    (println "X: " x " Y: " y)
    (println "S: " start-x " E: " end-x)
    (swap! state assoc :board
           {:w          width
            :h          height
            :vertical   (take-while (partial > end-x) (iterate (partial + rect-width) start-x))
            :horizontal (take-while (partial > end-y) (iterate (partial + rect-height) start-y))})))


(defn draw-grid [state]
  ;(draw-board state)
  (let [scale   (:scale @state)
          x       (:x @state)
          y       (:y @state)
          ;rect-width*  (/ rect-width scale)
          ;rect-height (/ rect-height scale)
          width   (/ (.-innerWidth js/window) scale)
          height  (/ (.-innerHeight js/window) scale)
          start-x (* (js/Math.floor (/ (- (- x) width) rect-width)) rect-width)
          end-x   (* (js/Math.floor (/ (+ (- x) (* width 2)) rect-width)) rect-width)
          start-y (* (js/Math.floor (/ (- (- y) height) rect-height)) rect-height)
          end-y   (* (js/Math.floor (/ (+ (- y) (* height 2)) rect-height)) rect-height)]
      (println "X: " x " Y: " y)
      (println "S: " start-x " E: " end-x)
      (swap! state assoc :rects
             (for [x (take-while (partial > end-x) (iterate (partial + rect-width) start-x))
                   y (take-while (partial > end-y) (iterate (partial + rect-height) start-y))]
               [x y]))
      (.batchDraw (:stage @state))))


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
            new-scale   (cond
                          (<= new-scale 0.15) 0.15
                          (>= new-scale 3.5) 3.5
                          :else new-scale)]
        (do
          (swap! state assoc
                 :scale new-scale
                 :x (* (- (- (:x mouse-point)
                             (/ (.-x pointer) new-scale))) new-scale)
                 :y (* (- (- (:y mouse-point)
                             (/ (.-y pointer) new-scale))) new-scale))))
      (do
        (swap! state update :x - (.-deltaX e))
        (swap! state update :y - (.-deltaY e))))
    (.batchDraw stage)))


(defn stage-drag-scale-comp []
  (let [state (r/atom {:scale 1
                       :x     0
                       :y     0})]
    (r/create-class
     {:component-did-mount (fn []
                             (draw-grid state)
                             (.then img-p #(swap! state assoc :img-grid %))
                             #_(js/setInterval (fn [] (println @state)) 1000))
      :reagent-render      (fn []
                             [c/stage {:width         (.-innerWidth js/window)
                                       :height        (.-innerHeight js/window)
                                       :draggable     true
                                       :ref           #(swap! state assoc :stage %)
                                       :scale-x       (:scale @state)
                                       :scale-y       (:scale @state)
                                       :x             (:x @state)
                                       :y             (:y @state)
                                       :on-wheel      #(do
                                                         (wheel % state)
                                                         (draw-grid state))
                                       :on-drag-start #(when (= "Stage" (.getClassName (.-target %)))
                                                         (println "Drag started")
                                                         (swap! state assoc
                                                                :x (.x (.-target %))
                                                                :y (.y (.-target %)))
                                                         (draw-grid state))
                                       :on-drag-end   #(when (= "Stage" (.getClassName (.-target %)))
                                                         (swap! state assoc
                                                                :x (.x (.-target %))
                                                                :y (.y (.-target %)))
                                                         (draw-grid state))}
                              [c/layer
                               ;(doall
                               ; (for [x (:vertical (:board @state))
                               ;       y (:horizontal (:board @state))
                               ;       :when (not= x y)]
                               ;   ^{:key [y x]}
                               ;   [c/line {:points       [x y x (:h (:board @state))]
                               ;            :stroke       "grey"
                               ;            :stroke-width 0.5}]))
                               ;(doall
                               ; (for [x (:vertical (:board @state))
                               ;       y (:horizontal (:board @state))]
                               ;   ^{:key [x y]}
                               ;   [c/line {:points       [x y (:w (:board @state)) y]
                               ;            :stroke       "grey"
                               ;            :stroke-width 0.5}]))
                               (doall
                                (for [position (:rects @state)]
                                  ^{:key position}
                                  [c/rect {:x            (first position)
                                           :y            (second position)
                                           :width        rect-width
                                           :height       rect-height
                                           :stroke-width 5
                                           :opacity      0.1
                                           :stroke       "grey"}]))]
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
                               [c/rect {:x         100
                                        :y         100
                                        :width     100
                                        :height    100
                                        :draggable true
                                        :fill      "red"
                                        :ref       #(swap! state assoc :rect %)}]]])})))
