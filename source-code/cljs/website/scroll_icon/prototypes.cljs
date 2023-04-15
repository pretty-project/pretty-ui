
(ns website.scroll-icon.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:color (keyword)}
  [icon-props]
  (merge {:color "#FFFFFF"}
         (param icon-props)))
