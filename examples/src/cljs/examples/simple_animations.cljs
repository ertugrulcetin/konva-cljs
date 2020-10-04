(ns examples.simple-animations
  (:require [konva-cljs.components :as c]
            [reagent.core :as r]))


(defn- simple-animation-comp []
  (let [state       (r/atom {})
        change-size #(.to (:rect @state) #js {:scaleX   (+ (Math/random) 0.8)
                                              :scaleY   (+ (Math/random) 0.8)
                                              :duration 0.2})]
    (fn []
      [c/stage {:width  (.-innerWidth js/window)
                :height (.-innerHeight js/window)}
       [c/layer
        [c/rect {:width         50
                 :height        50
                 :draggable     true
                 :fill          "green"
                 :ref           #(swap! state assoc :rect %)
                 :on-drag-start change-size
                 :on-drag-end   change-size}]]])))
