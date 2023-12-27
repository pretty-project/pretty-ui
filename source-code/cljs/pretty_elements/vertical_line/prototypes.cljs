
(ns pretty-elements.vertical-line.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-props-prototype
  ; @ignore
  ;
  ; @param (map) line-props
  ; {:color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:fill-color (keyword or string)
  ;  :height (keyword, px or string)
  ;  :strength (px)}
  [{:keys [color] :as line-props}]
  (merge {:fill-color (or color :muted)
          :height     :parent
          :strength   1}
         (-> line-props)))
