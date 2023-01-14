
(ns website.copyright-label.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-props-prototype
  ; @param (map) component-props
  ;
  ; @return (map)
  ; {:color (keyword or string)}
  [component-props]
  (merge {:color :inherit}
         (param component-props)))