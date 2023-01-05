
(ns layouts.sidebar-b.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ; {:position (keyword)
  ;  :viewport-min (px)}
  [layout-props]
  (merge {:position     :left
          :viewport-min 720}
         (param layout-props)))
