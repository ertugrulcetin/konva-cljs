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
    (swap! state assoc :board
           {:w          width
            :h          height
            :vertical   (take-while (partial > end-x) (iterate (partial + rect-width) start-x))
            :horizontal (take-while (partial > end-y) (iterate (partial + rect-height) start-y))})))


(defn draw-cool-grids [state]
  (let [x             (:x @state)
        y             (:y @state)
        width         (.-innerWidth js/window)
        height        (.-innerHeight js/window)
        x-mod         (mod x rect-width)
        y-mod         (mod y rect-height)
        v-lines-count (/ width rect-width)
        h-lines-count (/ height rect-height)
        v-lines       (if true #_(zero? x-mod)
                        (take (inc v-lines-count) (iterate (partial + rect-width) (- x)))
                        (let [lines (take (dec v-lines-count) (iterate (partial + rect-width) (- (+ x x-mod))))]
                          (concat [(- x)] lines [(+ (last lines) (- rect-width x-mod))])))
        h-lines       (if true #_(zero? x-mod)
                        (take (inc h-lines-count) (iterate (partial + rect-height) (- y)))
                        (let [lines (take (dec h-lines-count) (iterate (partial + rect-width) (- (+ y y-mod))))]
                          (concat [(- y)] lines [(+ (last lines) (- rect-height y-mod))])))]
    ;(swap! state assoc :v-lines v-lines :h-lines h-lines)
    (swap! state assoc :v-lines v-lines :h-lines h-lines)
    #_(.batchDraw (:stage state))))


(defn draw-grid [state]
  (draw-cool-grids state)
  ;(draw-board state)
  #_(let [scale   (:scale @state)
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
        (swap! state update :y - (.-deltaY e))
        (println {:x (- (:x @state))
                  :y (- (:y @state))})))
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
                                                         (swap! state assoc
                                                                :x (.x (.-target %))
                                                                :y (.y (.-target %)))
                                                         (draw-grid state))
                                       :on-drag-end   #(when (= "Stage" (.getClassName (.-target %)))
                                                         (swap! state assoc
                                                                :x (.x (.-target %))
                                                                :y (.y (.-target %)))
                                                         (draw-grid state))}
                              #_[c/layer
                               (let [scale        (:scale @state)
                                     size         128
                                     bound        (js/Math.pow 2 14)
                                     stroke-width (if (> (/ 0.5 scale) 0.5)
                                                    0.5
                                                    (/ 0.5 scale))]
                                 [:<>
                                  (doall
                                   (for [[i l] (map-indexed vector (take-while #(<= % bound) (iterate (partial + size) (- bound))))]
                                     ^{:key i}
                                     [c/line {:points       [l (- bound) l bound]
                                              :stroke       "grey"
                                              :stroke-width stroke-width}]))
                                  (doall
                                   (for [[i l] (map-indexed vector (take-while #(<= % bound) (iterate (partial + size) (- bound))))]
                                     ^{:key i}
                                     [c/line {:points       [(- bound) l bound l]
                                              :stroke       "grey"
                                              :stroke-width stroke-width}]))])
                               ;(doall
                               ; (for [x (:v-lines @state)]
                               ;   ^{:key x}
                               ;   [c/line {:points       [x 0 x (.-innerHeight js/window)]
                               ;            :stroke       "grey"
                               ;            :stroke-width 0.5}]))
                               ;(doall
                               ; (for [y (:h-lines @state)]
                               ;   ^{:key y}
                               ;   [c/line {:points       [0 y (.-innerWidth js/window) y]
                               ;            :stroke       "grey"
                               ;            :stroke-width 0.5}]))
                               ]
                              [c/layer
                               [c/shape {:x            0
                                         :y            0
                                         :fill         "#00D2FF"
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
