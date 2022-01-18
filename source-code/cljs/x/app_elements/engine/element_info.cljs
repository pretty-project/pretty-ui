
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.27
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-info
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:helper (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [helper]}]
  (if helper [:div.x-element--helper [components/content {:content helper}]]))

(defn- element-info-tooltip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:info-tooltip (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ {:keys [info-tooltip]}]
  (if info-tooltip [:div.x-element--info-tooltip
                     [:i.x-element--info-tooltip--icon :info_outline]
                     [:div.x-element--info-tooltip--body
                        [:div.x-element--info-tooltip--content [components/content {:content info-tooltip}]]]]))
