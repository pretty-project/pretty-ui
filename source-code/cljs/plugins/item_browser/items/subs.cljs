
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.subs
    (:require [plugins.plugin-handler.items.subs :as items.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/get-item db :my-browser "my-item")
  ;
  ; @return (map)
  [db [_ browser-id item-id]]
  (r items.subs/get-item db browser-id item-id))

(defn export-item
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/export-item db :my-browser "my-item")
  ;
  ; @return (namespaced map)
  [db [_ browser-id item-id]]
  (r items.subs/export-item db browser-id item-id))

(defn item-downloaded?
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/item-downloaded? db :my-browser "my-item")
  ;
  ; @return (boolean)
  [db [_ browser-id item-id]]
  (r items.subs/item-downloaded? db browser-id item-id))

(defn get-item-dex
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/get-item-dex db :my-browser "my-item")
  ;
  ; @return (integer)
  [db [_ browser-id item-id]]
  (r items.subs/get-item-dex db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
;  [:item-browser/get-item :my-browser "my-item"]
(r/reg-sub :item-browser/get-item get-item)
