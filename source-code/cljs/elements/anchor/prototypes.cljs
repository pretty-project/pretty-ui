
(ns elements.anchor.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn anchor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) anchor-props
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :font-size (keyword)
  ;  :line-height (keyword)}
  [anchor-props]
  (merge {:color       :primary
          :font-size   :s
          :line-height :normal}
         (param anchor-props)))
