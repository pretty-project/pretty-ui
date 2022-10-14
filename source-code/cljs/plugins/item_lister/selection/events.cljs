
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
              [re-frame.api                            :as r :refer [r]]))



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
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection! :my-lister "my-item")
  ;
  ; @return (map)
  [db [_ lister-id item-id]]
  (r selection.events/toggle-item-selection! db lister-id item-id))

(defn toggle-single-item-selection!
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/toggle-single-item-selection! :my-lister "my-item")
  ;
  ; @return (map)
  [db [_ lister-id item-id]]
  (r selection.events/toggle-single-item-selection! db lister-id item-id))

(defn toggle-limited-item-selection!
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ; @param (integer) selection-limit
  ;
  ; @usage
  ;  (r item-lister/toggle-limited-item-selection! :my-lister "my-item" 8)
  ;
  ; @return (map)
  [db [_ lister-id item-id selection-limit]]
  (r selection.events/toggle-limited-item-selection! db lister-id item-id selection-limit))

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

(defn import-selection!
  ; @param (keyword) lister-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @usage
  ;  (r item-lister/import-selection! db :my-lister ["my-item" "your-item"])
  ;
  ; @return (map)
  [db [_ lister-id selected-item-ids]]
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
  (r selection.events/import-single-selection! db lister-id selected-item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/select-all-items! :my-lister]
(r/reg-event-db :item-lister/select-all-items! select-all-items!)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
;  [:item-lister/toggle-item-selection! :my-lister "my-item"]
(r/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
;  [:item-lister/toggle-single-item-selection! :my-lister "my-item"]
(r/reg-event-db :item-lister/toggle-single-item-selection! toggle-single-item-selection!)


; @param (keyword) lister-id
; @param (string) item-id
; @param (integer) selection-limit
;
; @usage
;  [:item-lister/toggle-limited-item-selection! :my-lister "my-item" 8]
(r/reg-event-db :item-lister/toggle-limited-item-selection! toggle-limited-item-selection!)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/discard-selection! :my-lister]
(r/reg-event-db :item-lister/discard-selection! discard-selection!)
