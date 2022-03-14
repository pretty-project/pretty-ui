
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubbles.prototypes
    (:require [mid-fruits.candy :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ;
  ; @return (map)
  ;  {:autopop? (boolean)
  ;   :hide-animated? (boolean)
  ;   :initializer (metamorphic-event)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [bubble-id bubble-props]
  (merge {:autopop?    true
          :initializer [:ui/initialize-bubble! bubble-id]
          :user-close? true}
         (param bubble-props)
         {:hide-animated?   true
          :reveal-animated? true
          :update-animated? true}))
