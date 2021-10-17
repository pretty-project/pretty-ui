
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.29
; Description:
; Version: v1.6.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-layouts
    (:require [mid-fruits.css                   :as css]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-ui.element                 :as element]
              [x.app-ui.surface-control-sidebar :refer [view] :rename {view surface-control-sidebar}]
              [x.app-ui.surface-geometry        :as geometry]))



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
    (if (geometry/surface-props->render-control-sidebar? surface-props)
        {:style {:paddingLeft (css/px geometry/SURFACE-CONTROL-SIDEBAR-WIDTH)}})
    [element/element-content surface-id surface-props]])

(defn- surface-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:content (component)}
  ; @param (map) view-props
  ;  {:padding-top (integer)
  ;   :render-control-sidebar? (boolean)
  ;   :render-control-surface? (boolean)
  ;   :render-surface-header? (boolean)}
  ;
  ; @return (hiccup)
  [surface-id surface-props]
  (let [padding-top (geometry/surface-props->surface-structure-padding-top surface-props)]
       [:div.x-app-surface--element--structure
         {:style {:padding-top (css/px padding-top)}}
         [:div.x-app-surface--element--background]
         [surface-content surface-id surface-props]
         (if (geometry/surface-props->render-control-sidebar? surface-props)
             [surface-control-sidebar surface-id surface-props])]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (hiccup)
  [surface-id surface-props]
  [:div (surface-attributes surface-id surface-props)
        [surface-structure surface-id surface-props]])
