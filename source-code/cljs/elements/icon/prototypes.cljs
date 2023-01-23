
(ns elements.icon.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-props-prototype
  ; @ignore
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ; {:icon-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)}
  [icon-props]
  (merge {:icon-color  :default
          :icon-family :material-symbols-outlined
          :icon-size   :m}
         (param icon-props)))
