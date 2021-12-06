
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.29
; Description:
; Version: v1.8.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-layouts
    (:require [mid-fruits.css   :as css]
              [x.app-core.api   :as a :refer [r]]
              [x.app-ui.element :as element]))



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

(defn- surface-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (hiccup)
  [surface-id surface-props]
  [:div.x-app-surface--element--content
    [element/element-content surface-id surface-props]])

(defn- surface-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (hiccup)
  [surface-id surface-props]
  [:div.x-app-surface--element--structure
    [surface-content surface-id surface-props]])

(defn surface-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (hiccup)
  [surface-id surface-props]
  [:div (surface-attributes surface-id surface-props)
        [surface-structure  surface-id surface-props]])
