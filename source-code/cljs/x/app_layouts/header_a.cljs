
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.02
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.header-a
    (:require [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [icon label]}]
  [elements/label ::header-label
                  {:content label :icon icon :color :muted :font-size :m :font-weight :extra-bold :layout :fit}])

(defn- header-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;
  ; @return (hiccup)
  [header-id header-props]
  [:div.x-header-a [header-label header-id header-props]])

(defn header
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  ([header-props]
   [header nil header-props])

  ([header-id header-props]
   [header header-id header-props]))
