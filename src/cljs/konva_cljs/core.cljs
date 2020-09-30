(ns konva-cljs.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [goog.dom :as dom]
            [konva-cljs.views :as views]
            ["react-konva" :as rk]))



(defn ^:dev/after-load mount-root []
  (let [root-el (dom/getElement "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))


(defn init []
  (println "ertu")
  (mount-root))
