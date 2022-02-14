
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.01
; Description:
; Version: v0.5.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.focusable
    (:require [x.app-core.api        :as a]
              [x.app-environment.api :as environment]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id targetable/element-id->target-id environment/focus-element!))

(a/reg-fx :elements/focus-element! focus-element!)

(defn blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id targetable/element-id->target-id environment/blur-element!))

(a/reg-fx :elements/blur-element! blur-element!)
