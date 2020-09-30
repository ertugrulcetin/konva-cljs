(ns konva-cljs.core
  (:require [clojure.string :as str]
            ["react-konva" :as rk]
            ["konva" :default k]))


(def filters (reduce-kv
              (fn [acc k v]
                (assoc acc (-> k str/lower-case keyword) v))
              {}
              (js->clj (.-Filters k))))


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


(defn ellipse [ctx {:keys [x y radius-x radius-y rotation start-angle end-angle anti-clockwise?]}]
  (doto ctx (.ellipse x y radius-x radius-y rotation start-angle end-angle anti-clockwise?)))


(defn fill [ctx]
  (doto ctx .fill))


(defn fill-text
  [ctx {:keys [text x y max-width]}]
  (doto ctx (.fillText text x y max-width)))


(defn get-image-data [ctx {:keys [sx sy sw sh]}]
  (doto ctx (.getImageData sx sy sw sh)))


(defn create-image-data
  ([ctx image-data]
   (doto ctx (.createImageData ctx image-data)))
  ([ctx width height]
   (doto ctx (.createImageData ctx width height))))


(defn line-to
  [ctx x y]
  (doto ctx (.lineTo x y)))


(defn move-to
  [ctx x y]
  (doto ctx (.moveTo x y)))


(defn put-image-data
  ([ctx image-data dx dy]
   (doto ctx (.putImageData ctx image-data dx dy)))
  ([ctx image-data {:keys [dirty-x dirty-y dirty-w dirty-h]}]
   (doto ctx (.putImageData ctx image-data dirty-x dirty-y dirty-w dirty-h))))


(defn quadratic-curve-to
  [ctx {:keys [cpx cpy x y]}]
  (doto ctx (.quadraticCurveTo cpx cpy x y)))


(defn rect [ctx {:keys [x y w h]}]
  (doto ctx (.rect x y w h)))


(defn restore [ctx]
  (doto ctx .restore))


(defn rotate [ctx angle]
  (doto ctx (.rotate angle)))


(defn save [ctx]
  (doto ctx .save))


(defn scale [ctx x y]
  (doto ctx (.scale x y)))


(defn set-line-dash [ctx segments]
  (doto ctx (.setLineDash segments)))


(defn set-transform
  ([ctx matrix]
   (doto ctx (.setTransform matrix)))
  ([ctx m11 m12 m21 m22 dx dy]
   (doto ctx (.setTransform m11 m12 m21 m22 dx dy))))


(defn stroke [ctx]
  (doto ctx .stroke))


(defn stroke-text
  ([ctx text x y]
   (doto ctx (.strokeText text x y)))
  ([ctx text x y max-width]
   (doto ctx (.strokeText text x y max-width))))


(defn transform [ctx {:keys [m11 m12 m21 m22 dx dy]}]
  (doto ctx (.transform m11 m12 m21 m22 dx dy)))


(defn translate [ctx x y]
  (doto ctx (.translate x y)))


(defn fill-stroke-shape [ctx shape]
  (doto ctx (.fillStrokeShape shape)))

