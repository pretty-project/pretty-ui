
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.3.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler
    (:require [x.app-details :as details]
              [x.server-core.event-handler :as event-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-SERVER-ERROR "Something went wrong :(")



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) error-props
  [error-prop]
  (println details/app-codename DEFAULT-SERVER-ERROR))

(event-handler/reg-fx_ :core/error-catched error-catched)
