
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.selection.events
    (:require [engines.engine-handler.selection.events :as selection.events]
              [re-frame.api                            :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.selection.events
(def select-all-items!              selection.events/select-all-items!)
(def select-item!                   selection.events/select-item!)
(def toggle-item-selection!         selection.events/toggle-item-selection!)
(def toggle-single-item-selection!  selection.events/toggle-single-item-selection!)
(def toggle-limited-item-selection! selection.events/toggle-limited-item-selection!)
(def discard-selection!             selection.events/discard-selection!)
(def disable-selected-items!        selection.events/disable-selected-items!)
(def import-selection!              selection.events/import-selection!)
(def import-single-selection!       selection.events/import-single-selection!)
(def import-limited-selection!      selection.events/import-limited-selection!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
; [:item-lister/select-all-items! :my-lister]
(r/reg-event-db :item-lister/select-all-items! select-all-items!)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
; [:item-lister/toggle-item-selection! :my-lister "my-item"]
(r/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
; [:item-lister/toggle-single-item-selection! :my-lister "my-item"]
(r/reg-event-db :item-lister/toggle-single-item-selection! toggle-single-item-selection!)

; @param (keyword) lister-id
; @param (string) item-id
; @param (integer) selection-limit
;
; @usage
; [:item-lister/toggle-limited-item-selection! :my-lister "my-item" 8]
(r/reg-event-db :item-lister/toggle-limited-item-selection! toggle-limited-item-selection!)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/discard-selection! :my-lister]
(r/reg-event-db :item-lister/discard-selection! discard-selection!)
