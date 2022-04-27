
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :horizontal-align (keyword)
  ;   :reveal-animated? (boolean)
  ;   :trim-content? (boolean)
  ;   :update-animated? (boolean)}
  [surface-props]
  (merge {:trim-content?    false
          :horizontal-align :center}
         (param surface-props)
         {:hide-animated?   false
          :reveal-animated? false
          :update-animated? false}))
