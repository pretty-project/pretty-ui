
(ns components.color-picker.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-props-prototype
  ; @param (map) picker-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [color-stamp] :as picker-props}]
  (merge {:placeholder :choose-color!}
         (param picker-props)
         {:color-stamp (merge {:height :l :width :l}
                              (param color-stamp))}))
