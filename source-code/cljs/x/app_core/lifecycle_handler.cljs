;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.29
; Description:
; Version: v1.1.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.lifecycle-handler
    (:require [x.mid-core.lifecycle-handler :as lifecycle-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def get-period-events  lifecycle-handler/get-period-events)
(def reg-lifecycles!    lifecycle-handler/reg-lifecycles!)
(def import-lifecycles! lifecycle-handler/import-lifecycles!)
