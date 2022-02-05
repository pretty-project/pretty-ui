
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.08
; Description:
; Version: v1.1.2
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.engine
    (:require [x.app-core.api  :as a]
              [x.mid-db.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.engine
(def item-path->cofx-path engine/item-path->cofx-path)
(def subscribe-item       engine/subscribe-item)
(def subscribed-item      engine/subscribed-item)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def get-item-count       engine/get-item-count)
(def get-applied-item     engine/get-applied-item)
(def empty-db!            engine/empty-db!)
(def copy-item!           engine/copy-item!)
(def move-item!           engine/move-item!)
(def set-item!            engine/set-item!)
(def set-vector-item!     engine/set-vector-item!)
(def remove-item!         engine/remove-item!)
(def remove-vector-item!  engine/remove-vector-item!)
(def remove-item-n!       engine/remove-item-n!)
(def inc-item-n!          engine/inc-item-n!)
(def dec-item-n!          engine/dec-item-n!)
(def apply!               engine/apply!)
(def distribute-items!    engine/distribute-items!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-db]
(a/reg-sub :db/get-db get-db)

; @usage
;  [:db/get-item [:my-item]]
(a/reg-sub :db/get-item get-item)

; @usage
;  [:db/item-exists? [:my-item]]
(a/reg-sub :db/item-exists? item-exists?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/empty-db!]
(a/reg-event-db :db/empty-db! empty-db!)

; @usage
;  [:db/move-item! [:move-from] [:move-to]]
(a/reg-event-db :db/move-item! move-item!)

; @usage
;  [:db/set-item! [:my-item] "My value"]
(a/reg-event-db :db/set-item! set-item!)

; @usage
;  [:db/set-vector-item! [:my-item :0] "My value"]
(a/reg-event-db :db/set-vector-item! set-vector-item!)

; @usage
;  [:db/remove-item! [:my-item]]
(a/reg-event-db :db/remove-item! remove-item!)

; @usage
;  [:db/remove-vector-item! [:my-item 0]]
(a/reg-event-db :db/remove-vector-item! remove-vector-item!)

; @usage
;  [:db/remove-item-n! [[:my-item] [...]]]
(a/reg-event-db :db/remove-item-n! remove-item-n!)

; @usage
;  [:db/inc-item-n! [[:my-item] [...]]]
(a/reg-event-db :db/inc-item-n! inc-item-n!)

; @usage
;  [:db/dec-item-n! [[:my-item] [...]]]
(a/reg-event-db :db/dec-item-n! dec-item-n!)

; @usage
;  [:db/apply! [:my-item] merge {}]
(a/reg-event-db :db/apply! apply!)

; @usage
;  [:db/distribute-items! {:apple "red" :banana "yellow" :item {:nested-item "Nested value"}}
;                         {:apple  [:apple-color]
;                          :banana [:banana-color]
;                          :item   {:nested-item [:nested-value]}}]
(a/reg-event-db :db/distribute-items! distribute-items!)
