(ns examples.draggable-text
  (:require [reagent.core :as r]
            [konva-cljs.components :as c]))


(defn draggable-text-comp []
  (let [state (r/atom {:x 50 :y 50 :dragging? false})]
    (fn []
      [c/stage {:width  (.-innerWidth js/window)
                :height (.-innerHeight js/window)}
       [c/layer
        [c/text {:text          "Draggable Text"
                 :x             (:x @state)
                 :y             (:y @state)
                 :font-size     20
                 :draggable     true
                 :fill          (if (:dragging? @state) "green" "black")
                 :on-drag-start #(swap! state assoc :dragging? true)
                 :on-drag-end   #(swap! state assoc
                                        :dragging? false
                                        :x (.x (.-target %))
                                        :y (.y (.-target %)))}]]])))
