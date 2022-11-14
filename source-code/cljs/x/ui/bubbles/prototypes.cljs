
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.bubbles.prototypes
    (:require [candy.api :refer [param]]))



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
          :on-bubble-rendered [:x.ui/initialize-bubble! bubble-id]
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
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:indent (map)
  ;   :preset (keyword)}
  [button-props]
  (merge {:indent {:bottom :xxs :vertical :xs}
          :preset :primary}
         (param button-props)))
