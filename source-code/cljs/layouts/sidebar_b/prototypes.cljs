
(ns layouts.sidebar-b.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  ; {:position (keyword)
  ;  :viewport-min (px)}
  [sidebar-props]
  (merge {:position     :left
          :viewport-min 720}
         (param sidebar-props)))
