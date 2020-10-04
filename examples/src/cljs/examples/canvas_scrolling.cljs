(ns examples.canvas-scrolling
  (:require [konva-cljs.components :as c]
            [reagent.core :as r]
            [goog.dom :as dom]
            [goog.object :as ob]))


(def padding 500)
(def height 3000)
(def width 3000)

(def state (atom {}))


(defn- reposition-stage []
  (let [scroll-container (dom/getElement "scroll-container")
        dx               (- (.-scrollLeft scroll-container) padding)
        dy               (- (.-scrollTop scroll-container) padding)
        stage            (:stage @state)]
    (set! (.-transform (.-style (.container stage))) (str "translate(" dx "px, " dy "px)"))
    (.x stage (- dx))
    (.y stage (- dy))
    (.batchDraw stage)))


(defn- canvas-scroll-comp []
  (r/create-class
   {:component-did-mount (fn []
                           (.addEventListener (dom/getElement "scroll-container") "scroll" reposition-stage)
                           (reposition-stage)
                           (doseq [c (vals (:circles @state))]
                             (.cache c)))
    :reagent-render      (fn []
                           [:div#scroll-container
                            [:div#large-container
                             [:div#container
                              [c/stage {:width  (+ (.-innerWidth js/window) (* 2 padding))
                                        :height (+ (.-innerHeight js/window) (* 2 padding))
                                        :ref    #(swap! state assoc :stage %)}
                               [c/layer
                                (for [n (range 200)]
                                  ^{:key n}
                                  [c/circle {:x         (* width (Math/random))
                                             :y         (* height (Math/random))
                                             :ref       #(swap! state assoc-in [:circles n] %)
                                             :radius    50
                                             :fill      "red"
                                             :draggable true
                                             :stroke    "black"}])]]]]])}))
