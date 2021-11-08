
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.02
; Description:
; Version: v0.2.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.headers
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  ([header-props]
   [header-a nil header-props])
   
  ([_ {:keys [icon label]}]
   [:div.x-header-a
     [elements/label {:content label :icon icon :color :muted :font-size :m :font-weight :extra-bold :layout :fit}]]))
