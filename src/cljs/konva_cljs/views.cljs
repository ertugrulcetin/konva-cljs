(ns konva-cljs.views
  (:require [konva-cljs.components :as kc]
            [konva-cljs.core :as ko]
            [reagent.core :as r]
            [goog.dom :as dom]
            [goog.object :as ob]
            [reagent.dom :as rdom]
            [clojure.string :as str]
            ["konva" :default k]))

;(println "A: " (:noise ko/filters))
(defn main-panel []
  [kc/stage {:width  (.-innerWidth js/window)
             :height (.-innerHeight js/window)}
   [kc/layer

    [kc/rect
     {
      :filters    (r/as-element [(ob/get (.-Filters k) "Noise")])
      :noise      1
      :x          200
      :y          10
      :width      50
      :height     50
      :fill       "green"
      :shadowBlur 10}]

    ]])


(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [main-panel] root-el)))


(defn init []
  (println "ertu")
  (mount-root))
