
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.01
; Description:
; Version: v0.2.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.targetable
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-id->target-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Egyes esetekben az elem DOM struktúrája nem teszi lehetővé, hogy az element-id
  ; az elem működését biztosító DOM elemet azonosítsa (pl.: input, button, ...)
  ;
  ; @param (keyword) element-id
  ;
  ; @example
  ;  (targetable/element-id->target-id :my-namespace/my-element)
  ;  =>
  ;  "my-namespace--my-element--target"
  ;
  ; @return (string)
  [element-id]
  (a/dom-value element-id "target"))

(defn element-id->target-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (let [target-id (element-id->target-id element-id)]
       (environment/element-id->element-disabled? target-id)))

(defn element-id->target-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [element-id]
  (not (element-id->target-disabled? element-id)))
