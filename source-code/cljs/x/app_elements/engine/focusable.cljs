
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.01
; Description:
; Version: v0.4.4
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.focusable
    (:require [x.app-core.api        :as a]
              [x.app-environment.api :as environment]
              [x.app-elements.engine.targetable :as targetable]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn blur-element-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (function)
  [_]
  ; Ha a kattintható elemeken a kattintás után is ott marad a fókusz, akkor
  ; az ENTER és SPACE billentyűk lenyomására újból megtörténik a kattintási
  ; esemény. Ennek elkerülése érdekében szükséges megszűntetni a fókuszt
  ; az elemen!
  #(environment/blur-element!))



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
