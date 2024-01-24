
(ns pretty-elements.horizontal-line.prototypes
    (:require [pretty-css.api :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-props-prototype
  ; @ignore
  ;
  ; @param (map) line-props
  ; {:color (keyword or string)}
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :strength (px)
  ;  :width (keyword, px or string)}
  [{:keys [color] :as line-props}]
  (merge {:fill-color (or color :muted)
          :strength   1
          :width      :auto}
         (-> line-props)))
