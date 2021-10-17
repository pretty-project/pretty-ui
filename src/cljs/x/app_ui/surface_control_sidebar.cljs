
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.30
; Description:
; Version: v0.4.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-control-sidebar
    (:require [mid-fruits.css            :as css]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-ui.surface-geometry :as geometry]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-surface-control-sidebar-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (map)
  ;  {:padding-top (integer)}
  [db [_ surface-id]]
  {:padding-top (r geometry/get-surface-header-height db surface-id)})

(a/reg-sub ::get-surface-control-sidebar-view-props get-surface-control-sidebar-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-control-sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;  {:control-sidebar (map)}
  ; @param (map) view-props
  ;  {:padding-top (integer)}
  ;
  ; @return (hiccup)
  [surface-id {:keys [control-sidebar] :as surface-props}
              {:keys [padding-top]}]
  [:div.x-app-surface--element--control-sidebar
    {:style {:padding-top (css/px padding-top)
             :width       (css/px geometry/SURFACE-CONTROL-SIDEBAR-WIDTH)}}
    [components/content surface-id control-sidebar]])

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ;
  ; @return (component)
  [surface-id surface-props]
  [components/subscriber surface-id
    {:component    #'surface-control-sidebar
     :static-props surface-props
     :subscriber   [::get-surface-control-sidebar-view-props surface-id]}])
