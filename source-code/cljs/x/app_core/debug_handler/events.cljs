
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.5.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.events
    (:require [x.app-core.event-handler :as event-handler]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- set-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) debug-mode
  ;
  ; @return (map)
  [db [_ debug-mode]]
  (assoc-in db [:core/debug-handler :meta-items :debug-mode] debug-mode))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/set-debug-mode! set-debug-mode!)