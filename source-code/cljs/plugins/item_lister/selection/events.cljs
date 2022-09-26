
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.selection.events
    (:require [plugins.plugin-handler.selection.events :as selection.events]
              [x.app-core.api                          :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/select-all-items! :my-lister)
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r selection.events/select-all-items! db lister-id))

(defn toggle-item-selection!
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection! :my-lister 42)
  ;
  ; @return (map)
  [db [_ lister-id item-dex]]
  (r selection.events/toggle-item-selection! db lister-id item-dex))

(defn toggle-single-item-selection!
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-single-item-selection! :my-lister 42)
  ;
  ; @return (map)
  [db [_ lister-id item-dex]]
  (r selection.events/toggle-single-item-selection! db lister-id item-dex))

(defn toggle-limited-item-selection!
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ; @param (integer) selection-limit
  ;
  ; @usage
  ;  (r item-lister/toggle-limited-item-selection! :my-lister 42 8)
  ;
  ; @return (map)
  [db [_ lister-id item-dex selection-limit]]
  (r selection.events/toggle-limited-item-selection! db lister-id item-dex selection-limit))

(defn discard-selection!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/discard-selection! :my-lister)
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r selection.events/discard-selection! db lister-id))

(defn disable-selected-items!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/disable-selected-items! :my-lister)
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r selection.events/disable-selected-items! db lister-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-imported-selection!
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/apply-imported-selection! db :my-lister)
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; XXX#8891
  (r selection.events/apply-imported-selection! db lister-id))

(defn import-selection!
  ; @param (keyword) lister-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @usage
  ;  (r item-lister/import-selection! db :my-lister ["my-item" "your-item"])
  ;
  ; @return (map)
  [db [_ lister-id selected-item-ids]]
  ; XXX#8891
  (r selection.events/import-selection! db lister-id selected-item-ids))

(defn import-single-selection!
  ; @param (keyword) lister-id
  ; @param (string) selected-item-id
  ;
  ; @usage
  ;  (r item-lister/import-single-selection! db :my-lister "my-item")
  ;
  ; @return (map)
  [db [_ lister-id selected-item-id]]
  ; XXX#8891
  (r selection.events/import-single-selection! db lister-id selected-item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/select-all-items! :my-lister]
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/toggle-item-selection! :my-lister 42]
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/toggle-single-item-selection! :my-lister 42]
(a/reg-event-db :item-lister/toggle-single-item-selection! toggle-single-item-selection!)


; @param (keyword) lister-id
; @param (integer) item-dex
; @param (integer) selection-limit
;
; @usage
;  [:item-lister/toggle-limited-item-selection! :my-lister 42 8]
(a/reg-event-db :item-lister/toggle-limited-item-selection! toggle-limited-item-selection!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/discard-selection! :my-lister]
(a/reg-event-db :item-lister/discard-selection! discard-selection!)
