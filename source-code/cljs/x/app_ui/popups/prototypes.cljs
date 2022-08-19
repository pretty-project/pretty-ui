
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)}
  [popup-props]
  (merge {}
         (param popup-props)
         {;:hide-animated?   true
           :hide-animated? false ; Ameddig nincsenek animaciok, addig ne kelljen várni ...
          :reveal-animated? true
          :update-animated? false}))
