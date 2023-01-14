
(ns layouts.sidebar-b.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-width (keyword)
  ;  :position (keyword)
  ;  :viewport-min (px)}
  [{:keys [border-color] :as sidebar-props}]
  (merge {:position     :left
          :viewport-min 720}
         (if border-color {:border-width :xxs})
         (param sidebar-props)))
