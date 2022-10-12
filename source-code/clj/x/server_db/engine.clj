
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.7.8
; Compatibility: x4.5.8



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-db.engine
    (:require [mid-fruits.candy :refer [return]]
              [re-frame.api     :as r]
              [x.mid-db.engine  :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.engine
(def subscribe-item       engine/subscribe-item)
(def subscribed-item      engine/subscribed-item)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def get-item-count       engine/get-item-count)
(def get-applied-item     engine/get-applied-item)
(def empty-db!            engine/empty-db!)
(def toggle-item!         engine/toggle-item!)
(def toggle-item-value!   engine/toggle-item-value!)
(def copy-item!           engine/copy-item!)
(def move-item!           engine/move-item!)
(def set-item!            engine/set-item!)
(def set-vector-item!     engine/set-vector-item!)
(def remove-item!         engine/remove-item!)
(def remove-vector-item!  engine/remove-vector-item!)
(def remove-item-n!       engine/remove-item-n!)
(def inc-item-n!          engine/inc-item-n!)
(def dec-item-n!          engine/dec-item-n!)
(def apply-item!          engine/apply-item!)
(def distribute-items!    engine/distribute-items!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-db]
(r/reg-sub :db/get-db get-db)

; @usage
;  [:db/get-item [:my-item]]
(r/reg-sub :db/get-item get-item)

; @usage
;  [:db/item-exists? [:my-item]]
(r/reg-sub :db/item-exists? item-exists?)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/empty-db!]
(r/reg-event-db :db/empty-db! empty-db!)

; @usage
;  [:db/toggle-item! [:my-item]]
(r/reg-event-db :db/toggle-item! toggle-item!)

; @usage
;  [:db/toggle-item-value! [:my-item] :my-value]
(r/reg-event-db :db/toggle-item-value! toggle-item-value!)

; @usage
;  [:db/move-item! [:move-from] [:move-to]]
(r/reg-event-db :db/move-item! move-item!)

; @usage
;  [:db/set-item! [:my-item] "My value"]
(r/reg-event-db :db/set-item! set-item!)

; @usage
;  [:db/set-vector-item! [:my-item :0] "My value"]
(r/reg-event-db :db/set-vector-item! set-vector-item!)

; @usage
;  [:db/remove-item! [:my-item]]
(r/reg-event-db :db/remove-item! remove-item!)

; @usage
;  [:db/remove-vector-item! [:my-item 0]]
(r/reg-event-db :db/remove-vector-item! remove-vector-item!)

; @usage
;  [:db/remove-item-n! [[:my-item ] [...]]]
(r/reg-event-db :db/remove-item-n! remove-item-n!)

; @usage
;  [:db/inc-item-n! [[:my-item] [...]]]
(r/reg-event-db :db/inc-item-n! inc-item-n!)

; @usage
;  [:db/dec-item-n! [[:my-item] [...]]]
(r/reg-event-db :db/dec-item-n! dec-item-n!)

; @usage
;  [:db/apply-item! [:my-item] merge {}]
(r/reg-event-db :db/apply-item! apply-item!)

; @usage
;  [:db/distribute-items! {:apple "red" :banana "yellow" :item {:nested-item "Nested value"}}
;                         {:apple  [:apple-color]
;                          :banana [:banana-color]
;                          :item   {:nested-item [:nested-value]}}]
(r/reg-event-db :db/distribute-items! distribute-items!)
