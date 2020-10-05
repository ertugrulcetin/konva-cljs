(ns examples.free-drawing
  (:require [reagent.core :as r]
            [konva-cljs.components :as c]))


(defn free-drawing-comp []
  (let [state (r/atom {:lines    []
                       :drawing? false
                       :tool     "pen"})]
    (fn []
      [:<>
       [c/stage
        {:width         (.-innerWidth js/window)
         :height        (.-innerHeight js/window)
         :on-mouse-down (fn [e]
                          (let [{:keys [x y]} (js->clj (.. (.-target e) getStage getPointerPosition)
                                                       :keywordize-keys true)]
                            (swap! state assoc :drawing? true)
                            (swap! state update :lines conj {:points [x y] :tool (:tool @state)})))
         :on-mouse-up   (fn [_] (swap! state assoc :drawing? false))
         :on-mouse-move (fn [e]
                          (when (:drawing? @state)
                            (let [stage (.getStage (.-target e))
                                  {:keys [x y]} (js->clj (.getPointerPosition stage) :keywordize-keys true)]
                              (swap! state update :lines
                                     (fn [coll v]
                                       (conj (vec (butlast coll))
                                             {:points (vec (concat (:points (last coll)) v))
                                              :tool   (:tool (last coll))}))
                                     [x y]))))}
        [c/layer
         [c/text
          {:text "Just start drawing"
           :x    5
           :y    30}]
         (doall
          (for [line (:lines @state)]
            ^{:key line}
            [c/line
             {:points                     (:points line)
              :stroke                     "#df4b26"
              :stroke-width               5
              :tension                    0.5
              :line-cap                   "round"
              :global-composite-operation (if (= (:tool line) "pen") "source-over" "destination-out")}]))]]
       [:select
        {:style     {:position "absolute"
                     :top      "5px"
                     :left     "5px"}
         :value     (:tool @state)
         :on-change (fn [e] (swap! state assoc :tool (-> e .-target .-value)))}
        [:option {:value "pen"} "Pen"]
        [:option {:value "eraser"} "Eraser"]]])))
