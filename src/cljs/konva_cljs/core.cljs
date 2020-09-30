(ns konva-cljs.core
  (:require ["react-konva" :as rk]))


(defn arc [ctx {:keys [x y r start-angle end-angle anti-clockwise?]}]
  (doto ctx (.arc x y r start-angle end-angle anti-clockwise?)))


(defn arc-to [ctx {:keys [x1 y1 x2 y2 radius]}]
  (doto ctx (.arcTo x1 y1 x2 y2 radius)))


(defn begin-path [ctx]
  (doto ctx .beginPath))


(defn bezier-curve-to [ctx {:keys [cp1x cp1y cp2x cp2y x y]}]
  (doto ctx (.bezierCurveTo cp1x cp1y cp2x cp2y x y)))


(defn clear-rect [ctx {:keys [x y w h]}]
  (doto ctx (.clearRect x y w h)))


(defn clip [ctx]
  (doto ctx .clip))


(defn close-path [ctx]
  (doto ctx .closePath))


(defn create-linear-gradient [ctx {:keys [x0 y0 x1 y1]}]
  (doto ctx (.createLinearGradient x0 y0 x1 y1)))


(defn create-pattern [ctx img repetition]
  (doto ctx (.createPattern img repetition)))


(defn create-radial-gradient [ctx {:keys [x0 y0 r0 x1 y1 r1]}]
  (doto ctx (.createRadialGradient x0 y0 r0 x1 y1 r1)))


(defn draw-image
  ([ctx img x y]
   (doto ctx (.drawImage img x y)))
  ([ctx img {:keys [x y w h
                    sx sy sw sh dx dy dw dh] :as params}]
   (condp = (count params)
     2 (. ctx (drawImage img x y))
     4 (. ctx (drawImage img x y w h))
     8 (. ctx (drawImage img sx sy sw sh dx dy dw dh)))
   ctx))


(defn quadratic-curve-to
  [ctx {:keys [cpx cpy x y]}]
  (doto ctx (.quadraticCurveTo cpx cpy x y)))


(defn move-to
  [ctx x y]
  (doto ctx (.moveTo x y)))


(defn line-to
  [ctx x y]
  (doto ctx (.lineTo x y)))


(defn fill-stroke-shape [ctx shape]
  (doto ctx (.fillStrokeShape shape)))

