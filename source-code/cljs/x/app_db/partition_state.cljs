
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.22
; Description:
; Version: v0.2.2
; Compatibility: x4.2.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.partition-state
    (:require [x.app-core.api           :as a]
              [x.mid-db.partition-state :as partition-state]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-state
(def partition-state->data-order          partition-state/partition-state->data-order)
(def partition-state->in-range-data-order partition-state/partition-state->in-range-data-order)
(def get-partition-state                  partition-state/get-partition-state)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.app-db/get-partition-state :my-partition/primary]
(a/reg-sub :x.app-db/get-partition-state get-partition-state)
