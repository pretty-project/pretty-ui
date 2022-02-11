
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.01
; Description:
; Version: v0.4.8
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
  (let [target-id (targetable/element-id->target-id element-id)]
       (environment/focus-element! target-id)))

(a/reg-fx :elements/focus-element! focus-element!)

(defn blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (let [target-id (targetable/element-id->target-id element-id)]
       (environment/blur-element! target-id)))

(a/reg-fx :elements/blur-element! blur-element!)
