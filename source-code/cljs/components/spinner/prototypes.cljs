
(ns components.spinner.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn spinner-props-prototype
  ; @param (map) spinner-props
  ;
  ; @return (map)
  ; {:color (keyword)
  ;  :size (keyword)}
  [spinner-props]
  (merge {:color :primary
          :size  :m}
         (param spinner-props)))
