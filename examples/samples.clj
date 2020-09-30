(ns samples)

(defn custom-shape []
      [n/stage {:width  (.-innerWidth js/window)
                :height (.-innerHeight js/window)}
       [n/layer
        [n/shape {:fill         "#00D2FF"
                  :stroke       "black"
                  :stroke-width "4"
                  :scene-func   (fn [ctx shape]
                                    (-> ctx
                                        (begin-path)
                                        (move-to 20 50)
                                        (line-to 220 80)
                                        (quadratic-curve-to {:cpx 150 :cpy 100 :x 260 :y 170})
                                        (close-path)
                                        (fill-stroke-shape shape)))}]]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def stars (r/atom (repeatedly 10 (fn [] {:id        (str (random-uuid))
                                          :x         (* (Math/random) (.-innerWidth js/window))
                                          :y         (* (Math/random) (.-innerHeight js/window))
                                          :rotation  (* (Math/random) 180)
                                          :dragging? false}))))

(defn stars-component []
      (let [find-and-update (fn [star dragging? coll]
                                (mapv #(if (= (:id %) (:id star))
                                         (assoc % :dragging? dragging?)
                                         %) coll))]
           [n/stage {:width  (.-innerWidth js/window)
                     :height (.-innerHeight js/window)}
            [n/layer
             [n/text {:text "Try to drag a star"}]
             (for [star @stars]
                  [n/star {:key             (:id star)
                           :id              (:id star)
                           :x               (:x star)
                           :y               (:y star)
                           :num-points      5
                           :inner-radius    20
                           :outer-radius    40
                           :fill            "#89b717"
                           :opacity         0.8
                           :draggable       true
                           :rotation        (:rotation star)
                           :shadow-color    "black"
                           :shadow-blur     10
                           :shadow-opacity  0.6
                           :shadow-offset-x (if (:dragging? star) 10 5)
                           :shadow-offset-y (if (:dragging? star) 10 5)
                           :scale-x         (if (:dragging? star) 1.2 1)
                           :scale-y         (if (:dragging? star) 1.2 1)
                           :on-drag-start   (fn [_] (reset! stars (find-and-update star true @stars)))
                           :on-drag-end     (fn [_] (reset! stars (find-and-update star false @stars)))}])]]))
