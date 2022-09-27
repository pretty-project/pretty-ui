
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
              [x.app-core.api                          :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-item-selection!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/toggle-item-selection! :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r selection.events/toggle-item-selection! db browser-id item-id))

(defn toggle-single-item-selection!
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/toggle-single-item-selection! :my-browser "my-item")
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
  ;  (r item-browser/toggle-limited-item-selection! :my-browser "my-item" 8)
  ;
  ; @return (map)
  [db [_ browser-id item-id selection-limit]]
  (r selection.events/toggle-limited-item-selection! db browser-id item-id selection-limit))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-selection!
  ; @param (keyword) browser-id
  ; @param (strings in vector) selected-item-ids
  ;
  ; @usage
  ;  (r item-browser/import-selection! db :my-browser ["my-item" "your-item"])
  ;
  ; @return (map)
  [db [_ browser-id selected-item-ids]]
  ; XXX#8891
  (r selection.events/import-selection! db browser-id selected-item-ids))

(defn import-single-selection!
  ; @param (keyword) browser-id
  ; @param (string) selected-item-id
  ;
  ; @usage
  ;  (r item-browser/import-single-selection! db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id selected-item-id]]
  ; XXX#8891
  (r selection.events/import-single-selection! db browser-id selected-item-id))
