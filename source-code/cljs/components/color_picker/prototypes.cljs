
(ns components.color-picker.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-props-prototype
  ; @param (map) picker-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [color-stamp] :as picker-props}]
  (merge {:click-effect :opacity
          :placeholder  :choose-color!}
         (param picker-props)
         {:color-stamp (merge {:height :l :width :l}
                              (param color-stamp))}))
