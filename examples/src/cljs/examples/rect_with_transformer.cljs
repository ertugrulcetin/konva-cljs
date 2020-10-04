(ns examples.rect-with-transformer
  (:require [konva-cljs.components :as c]
            [reagent.core :as r]))


(defn rect-with-transformer-comp []
  (let [state (r/atom {:x 100 :y 100})]
    (fn []
      (r/create-class
       {:component-did-mount (fn [] (.setNode (:tr @state) (:rect @state)))
        :reagent-render      (fn [] [c/stage {:width  (.-innerWidth js/window)
                                              :height (.-innerHeight js/window)}
                                     [c/layer
                                      [:<>
                                       [c/rect {:x                (:x @state)
                                                :y                (:y @state)
                                                :width            100
                                                :height           100
                                                :draggable        true
                                                :fill             "red"
                                                :ref              #(swap! state assoc :rect %)
                                                :on-transform-end #(let [rect (:rect @state)]
                                                                     (println "State: " {:x        (.x rect)
                                                                                         :y        (.y rect)
                                                                                         :rotation (.rotation rect)
                                                                                         :width    (.width rect)
                                                                                         :height   (.height rect)
                                                                                         :scale-x  (.scaleX rect)
                                                                                         :scale-y  (.scaleY rect)}))}]
                                       [c/transformer {:ref #(swap! state assoc :tr %)}]]]])}))))
