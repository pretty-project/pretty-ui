
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.items.events
    (:require [engines.engine-handler.items.events :as items.events]
              [re-frame.api                        :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.items.events
(def disable-items!    items.events/disable-items!)
(def enable-items!     items.events/enable-items!)
(def enable-all-items! items.events/enable-all-items!)

 

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-lister/disable-items! :my-lister ["my-item" "your-item"]]
(r/reg-event-db :item-lister/disable-items! disable-items!)

; @usage
; [:item-lister/enable-items! :my-lister ["my-item" "your-item"]]
(r/reg-event-db :item-lister/enable-items! enable-items!)

; @usage
; [:item-lister/enable-all-items! :my-lister]
(r/reg-event-db :item-lister/enable-all-items! enable-all-items!)
