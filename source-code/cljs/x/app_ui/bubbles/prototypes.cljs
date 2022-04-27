
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
  ;  {:autoclose? (boolean)
  ;   :hide-animated? (boolean)
  ;   :on-bubble-rendered (metamorphic-event)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [bubble-id bubble-props]
  (merge {:autoclose?         true
          :on-bubble-rendered [:ui/initialize-bubble! bubble-id]
          :user-close?        true}
         (param bubble-props)
         {:hide-animated?   true
          :reveal-animated? true
          :update-animated? true}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn primary-button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) button-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:indent (map)
  ;   :on-click (metamorphic-event)
  ;   :preset (keyword)}
  [bubble-id {:keys [on-click] :as button-props}]
  (merge {:indent {:bottom :xxs :vertical :xs}
          :preset :primary}
         (param button-props)
         {:on-click {:dispatch-n [on-click [:ui/close-bubble! bubble-id]]}}))
