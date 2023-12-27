
(ns pretty-elements.horizontal-separator.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn separator-props-prototype
  ; @ignore
  ;
  ; @param (map) separator-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :width (keyword, px or string)}
  [separator-props]
  (merge {:color :default
          :width :auto}
         (-> separator-props)))
