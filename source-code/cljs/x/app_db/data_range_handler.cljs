
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.09.26
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5




;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.data-range-handler
    (:require [x.app-core.api              :as a]
              [x.mid-db.data-range-handler :as data-range-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-range-handler
(def data-cursor-value-in-threshold?   data-range-handler/data-cursor-value-in-threshold?)
(def get-data-cursor-high              data-range-handler/get-data-cursor-high)
(def get-data-cursor-low               data-range-handler/get-data-cursor-low)
(def data-range-passable?              data-range-handler/data-range-passable?)
(def get-first-data-item-id-in-range   data-range-handler/get-first-data-item-id-in-range)
(def get-first-data-item-in-range      data-range-handler/get-first-data-item-in-range)
(def get-last-data-item-id-in-range    data-range-handler/get-last-data-item-id-in-range)
(def get-last-data-item-in-range       data-range-handler/get-last-data-item-in-range)
(def get-first-data-item-id-post-range data-range-handler/get-first-data-item-id-post-range)
(def get-first-data-item-post-range    data-range-handler/get-first-data-item-post-range)
(def get-last-data-item-id-pre-range   data-range-handler/get-last-data-item-id-pre-range)
(def get-last-data-item-pre-range      data-range-handler/get-last-data-item-pre-range)
(def get-in-range-data-order           data-range-handler/get-in-range-data-order)
(def get-in-range-data-items           data-range-handler/get-in-range-data-items)
(def get-pre-range-data-order          data-range-handler/get-pre-range-data-order)
(def get-pre-range-data-items          data-range-handler/get-pre-range-data-items)
(def get-post-range-data-order         data-range-handler/get-post-range-data-order)
(def get-post-range-data-items         data-range-handler/get-post-range-data-items)
(def partition-ranged?                 data-range-handler/partition-ranged?)
(def step-data-cursor-high-bwd!        data-range-handler/step-data-cursor-high-bwd!)
(def step-data-cursor-high-fwd!        data-range-handler/step-data-cursor-high-fwd!)
(def step-data-cursor-low-bwd!         data-range-handler/step-data-cursor-low-bwd!)
(def step-data-cursor-low-fwd!         data-range-handler/step-data-cursor-low-fwd!)
(def trim-partition!                   data-range-handler/trim-partition!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-db :db/step-data-cursor-high-bwd! step-data-cursor-high-bwd!)
(a/reg-event-db :db/step-data-cursor-high-fwd! step-data-cursor-high-fwd!)
(a/reg-event-db :db/step-data-cursor-low-bwd!  step-data-cursor-low-bwd!)
(a/reg-event-db :db/step-data-cursor-low-fwd!  step-data-cursor-low-fwd!)
(a/reg-event-db :db/trim-partition!            trim-partition!)
