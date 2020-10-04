(ns konva-cljs.components
  (:require [reagent.core :as r]
            ["react-konva" :as rk]))


(def arc (r/adapt-react-class rk/Arc))
(def arrow (r/adapt-react-class rk/Arrow))
(def circle (r/adapt-react-class rk/Circle))
(def ellipse (r/adapt-react-class rk/Ellipse))
(def fast-layer (r/adapt-react-class rk/FastLayer))
(def group (r/adapt-react-class rk/Group))
(def image (r/adapt-react-class rk/Image))
(def label (r/adapt-react-class rk/Label))
(def layer (r/adapt-react-class rk/Layer))
(def line (r/adapt-react-class rk/Line))
(def path (r/adapt-react-class rk/Path))
(def rect (r/adapt-react-class rk/Rect))
(def regular-polygon (r/adapt-react-class rk/RegularPolygon))
(def ring (r/adapt-react-class rk/Ring))
(def shape (r/adapt-react-class rk/Shape))
(def sprite (r/adapt-react-class rk/Sprite))
(def star (r/adapt-react-class rk/Star))
(def stage (r/adapt-react-class rk/Stage))
(def tag (r/adapt-react-class rk/Tag))
(def text (r/adapt-react-class rk/Text))
(def text-path (r/adapt-react-class rk/TextPath))
(def transformer (r/adapt-react-class rk/Transformer))
(def wedge (r/adapt-react-class rk/Wedge))