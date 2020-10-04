# konva-cljs

A minimalistic ClojureScript interface to [react-konva](https://github.com/konvajs/react-konva)


konva-cljs is a ClojureScript library for drawing complex canvas graphics using React (**reagent**). It's a ClojureScript wrapper of [KonvaJS](https://konvajs.org/).


## Usage

[![Clojars Project](https://clojars.org/konva-cljs/latest-version.svg)](https://clojars.org/konva-cljs)


### Examples

You can find a couple of samples in [/examples](/examples/src/cljs/examples) **directory**.


```clojure
(require '[konva-cljs.core :as k]
         '[konva-cljs.components :as c])

(defn custom-shape-comp []
  [c/stage {:width  (.-innerWidth js/window)
            :height (.-innerHeight js/window)}
   [c/layer
    [c/shape {:fill         "#00D2FF"
              :stroke       "black"
              :stroke-width 4
              :scene-func   (fn [ctx shape]
                              (-> ctx
                                  (k/begin-path)
                                  (k/move-to 20 50)
                                  (k/line-to 220 80)
                                  (k/quadratic-curve-to {:cpx 150 :cpy 100 :x 260 :y 170})
                                  (k/close-path)
                                  (k/fill-stroke-shape shape)))}]]])
```
