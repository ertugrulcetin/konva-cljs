(ns examples.blurred-rect
  (:require [reagent.core :as r]
            [konva-cljs.core :refer [filters]]
            [konva-cljs.components :as c]))


(defn blurred-rect-comp []
  (let [state (r/atom {})]
    (fn []
      (r/create-class
       {:component-did-mount (fn []
                               (.cache (:rect @state))
                               (.. (:rect @state) getLayer batchDraw))
        :reagent-render      (fn [] [c/stage {:width  (.-innerWidth js/window)
                                              :height (.-innerHeight js/window)}
                                     [c/layer
                                      [c/rect {:filters     [(:blur filters)]
                                               :ref         #(swap! state assoc :rect %)
                                               :blur-radius 20
                                               :shadow-blur 10
                                               :x           200
                                               :y           10
                                               :width       50
                                               :height      50
                                               :fill        "green"}]]])}))))
