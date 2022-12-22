
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.items.events
    (:require [engines.engine-handler.items.events :as items.events]
              [re-frame.api                        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.events
(def disable-items!    items.events/disable-items!)
(def enable-items!     items.events/enable-items!)
(def enable-all-items! items.events/enable-all-items!)
(def disable-item!     items.events/disable-item!)
(def enable-item!      items.events/enable-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-browser/disable-items! :my-browser ["my-item" "your-item"]]
(r/reg-event-db :item-browser/disable-items! disable-items!)

; @usage
; [:item-browser/enable-items! :my-browser ["my-item" "your-item"]]
(r/reg-event-db :item-browser/enable-items! enable-items!)

; @usage
; [:item-browser/enable-all-items! :my-browser]
(r/reg-event-db :item-browser/enable-all-items! enable-all-items!)

; @usage
; [:item-browser/disable-item! :my-browser "my-item"]
(r/reg-event-db :item-browser/disable-item! disable-item!)

; @usage
; [:item-browser/enable-item! :my-browser "my-item"]
(r/reg-event-db :item-browser/enable-item! enable-item!)
