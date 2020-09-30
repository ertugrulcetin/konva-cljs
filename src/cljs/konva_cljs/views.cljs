(ns konva-cljs.views
  (:require [konva-cljs.components :as kc]
            [konva-cljs.core :as ko]
            [reagent.core :as r]
            [goog.dom :as dom]
            [reagent.dom :as rdom]))


(defn main-panel []
  [kc/stage {:width  (.-innerWidth js/window)
             :height (.-innerHeight js/window)}
   [kc/layer
    [kc/shape {:fill         "#00D2FF"
               :stroke       "black"
               :stroke-width 4
               :scene-func   (fn [ctx shape]
                               (-> ctx
                                   (ko/begin-path)
                                   (ko/move-to 20 50)
                                   (ko/line-to 220 80)
                                   (ko/quadratic-curve-to {:cpx 150 :cpy 100 :x 260 :y 170})
                                   (ko/close-path)
                                   (ko/fill-stroke-shape shape)))}]]])


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (println "ertu")
  (mount-root))
