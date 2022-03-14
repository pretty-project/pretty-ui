
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :horizontal-align (keyword)
  ;   :reveal-animated? (boolean)
  ;   :trim-content? (boolean)
  ;   :update-animated? (boolean)}
  [surface-props]
  (merge {:hide-animated?   false
          :reveal-animated? false
          :trim-content?    false
          :update-animated? false
          :horizontal-align :center}
         (param surface-props)))
