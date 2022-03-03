
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.29
; Description:
; Version: v1.8.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-layouts
    (:require [mid-fruits.css       :as css]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-ui.element     :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (map)
  [surface-id surface-props]
  (element/element-attributes :surface surface-id surface-props))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:view (map)}
  [surface-id {:keys [view]}]
  [:div.x-app-surface--element--view
    [components/content surface-id view]])

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  [:div (surface-attributes surface-id surface-props)
        [surface-view       surface-id surface-props]])
