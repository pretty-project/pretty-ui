
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v1.0.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.lifecycle-handler
    (:require [x.mid-core.lifecycle-handler :as lifecycle-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def get-period-events lifecycle-handler/get-period-events)
(def reg-lifecycles    lifecycle-handler/reg-lifecycles)
