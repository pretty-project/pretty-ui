
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.06
; Description:
; Version: v0.3.6
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.bubble-body-presets
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-elements.api :as elements]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- primary-button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) button-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:on-click (metamorphic-event)
  ;   :preset (keyword)}
  [bubble-id {:keys [on-click] :as button-props}]
  (merge {:preset :primary-button}
         (param button-props)
         {:on-click {:dispatch-n [on-click [:ui/pop-bubble! bubble-id]]}}))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn state-changed-bubble-body
  ; @param (keyword) bubble-id
  ; @param (map) body-props
  ;  {:label (metamorphic-content)(opt)
  ;   :primary-button (map)(opt)
  ;    {:label (metamorphic-content)
  ;     :on-click (metamorphic-event)}}
  ;
  ; @usage
  ;  [ui/state-changed-bubble-body :my-bubble {...}]
  [bubble-id {:keys [label primary-button]}]
  [:<> (if label          [elements/label  {:content label}])
       (if primary-button [elements/button (primary-button-props-prototype bubble-id primary-button)])])
