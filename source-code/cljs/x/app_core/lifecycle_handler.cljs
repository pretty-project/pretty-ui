;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.29
; Description:
; Version: v0.9.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.lifecycle-handler
    (:require [x.app-core.event-handler     :as event-handler :refer [r]]
              [x.mid-core.lifecycle-handler :as lifecycle-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def get-period-events lifecycle-handler/get-period-events)
(def get-lifes         lifecycle-handler/get-lifes)
(def reg-lifecycles!   lifecycle-handler/reg-lifecycles!)
(def reg-event!        lifecycle-handler/reg-event!)
(def reg-lifecycles    lifecycle-handler/reg-lifecycles)
