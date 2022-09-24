
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
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-browser/toggle-item-selection! :my-browser 42)
  ;
  ; @return (map)
  [db [_ browser-id item-dex]]
  (r selection.events/toggle-item-selection! db browser-id item-dex))



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
