
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.3.0
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler
    (:require [x.server-core.event-handler :as event-handler]
              [x.server-core.print-handler :as print-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-SERVER-ERROR "Something went wrong :(")



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-handled-fx
  :core/->error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) error-props
  (fn [error-props]
      (print-handler/console DEFAULT-SERVER-ERROR)))
