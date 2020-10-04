(ns examples.stars
  (:require [reagent.core :as r]
            [konva-cljs.components :as c]))



(defn stars-comp []
  (let [stars           (r/atom (repeatedly 10 (fn [] {:id        (str (random-uuid))
                                                       :x         (* (Math/random) (.-innerWidth js/window))
                                                       :y         (* (Math/random) (.-innerHeight js/window))
                                                       :rotation  (* (Math/random) 180)
                                                       :dragging? false})))
        find-and-update (fn [star dragging? coll]
                          (mapv #(if (= (:id %) (:id star))
                                   (assoc % :dragging? dragging?)
                                   %) coll))]
    (fn []
      [c/stage {:width  (.-innerWidth js/window)
                :height (.-innerHeight js/window)}
       [c/layer
        [c/text {:text "Try to drag a star"}]
        (for [star @stars]
          [c/star {:key             (:id star)
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
                   :on-drag-end     (fn [_] (reset! stars (find-and-update star false @stars)))}])]])))
