
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.selection.events
    (:require [plugins.plugin-handler.selection.events :as selection.events]
              [re-frame.api                            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-all-items!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r select-all-items! :my-browser)
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r selection.events/select-all-items! db browser-id))

(defn toggle-item-selection!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r toggle-item-selection! :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r selection.events/toggle-item-selection! db browser-id item-id))

(defn toggle-single-item-selection!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r toggle-single-item-selection! :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r selection.events/toggle-single-item-selection! db browser-id item-id))

(defn toggle-limited-item-selection!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ; @param (integer) selection-limit
  ;
  ; @usage
  ;  (r toggle-limited-item-selection! :my-browser "my-item" 8)
  ;
  ; @return (map)
  [db [_ browser-id item-id selection-limit]]
  (r selection.events/toggle-limited-item-selection! db browser-id item-id selection-limit))

(defn discard-selection!
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r discard-selection! :my-browser)
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r selection.events/discard-selection! db browser-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-selection!
  ; @param (keyword) browser-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @usage
  ;  (r import-selection! db :my-browser ["my-item" "your-item"])
  ;
  ; @return (map)
  [db [_ browser-id selected-item-ids]]
  (r selection.events/import-selection! db browser-id selected-item-ids))

(defn import-single-selection!
  ; @param (keyword) browser-id
  ; @param (string) selected-item-id
  ;
  ; @usage
  ;  (r import-single-selection! db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id selected-item-id]]
  (r selection.events/import-single-selection! db browser-id selected-item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/select-all-items! :my-browser]
(r/reg-event-db :item-browser/select-all-items! select-all-items!)

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
;  [:item-browser/toggle-item-selection! :my-browser "my-item"]
(r/reg-event-db :item-browser/toggle-item-selection! toggle-item-selection!)

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
;  [:item-browser/toggle-single-item-selection! :my-browser "my-item"]
(r/reg-event-db :item-browser/toggle-single-item-selection! toggle-single-item-selection!)


; @param (keyword) browser-id
; @param (string) item-id
; @param (integer) selection-limit
;
; @usage
;  [:item-browser/toggle-limited-item-selection! :my-browser "my-item" 8]
(r/reg-event-db :item-browser/toggle-limited-item-selection! toggle-limited-item-selection!)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/discard-selection! :my-browser]
(r/reg-event-db :item-browser/discard-selection! discard-selection!)
