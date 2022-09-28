
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.selection.subs
    (:require [plugins.plugin-handler.selection.subs :as selection.subs]
              [x.app-core.api                        :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-browser/get-selected-item-count db :my-browser)
  ;
  ; @return (integer)
  [db [_ browser-id]]
  (r selection.subs/get-selected-item-count db browser-id))

(defn item-selected?
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/item-selected? db :my-browser "my-item")
  ;
  ; @return (boolean)
  [db [_ browser-id item-id]]
  (r selection.subs/item-selected? db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r item-browser/export-selection db :my-browser)
  ;  =>
  ;  ["my-item" "your-item"]
  ;
  ; @return (strings in vector)
  [db [_ browser-id]]
  (r selection.subs/export-selection db browser-id))

(defn export-single-selection
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (r item-browser/export-single-selection db :my-browser)
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ browser-id]]
  (r selection.subs/export-single-selection db browser-id))

(defn get-imported-selection
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (r item-browser/get-imported-selection db :my-browser)
  ;  =>
  ;  ["my-item" "your-item"]
  ;
  ; @return (strings in vector)
  [db [_ browser-id]]
  (r selection.subs/get-imported-selection db browser-id))
