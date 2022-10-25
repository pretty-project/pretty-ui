
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.selection.subs
    (:require [plugins.engine-handler.selection.subs :as selection.subs]
              [re-frame.api                          :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; @param (keyword) browser-id
  ;
  ; @example
  ;  (r export-selection db :my-browser)
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
  ;  (r export-single-selection db :my-browser)
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ browser-id]]
  (r selection.subs/export-single-selection db browser-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r get-selected-item-count db :my-browser)
  ;
  ; @return (integer)
  [db [_ browser-id]]
  (r selection.subs/get-selected-item-count db browser-id))

(defn all-items-selected?
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r all-items-selected? db :my-browser)
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r selection.subs/all-items-selected? db browser-id))

(defn all-downloaded-items-selected?
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r all-downloaded-items-selected? db :my-browser)
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r selection.subs/all-downloaded-items-selected? db browser-id))

(defn any-item-selected?
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r any-item-selected? db :my-browser)
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r selection.subs/any-item-selected? db browser-id))

(defn any-downloaded-item-selected?
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r any-downloaded-item-selected? db :my-browser)
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r selection.subs/any-downloaded-item-selected? db browser-id))

(defn no-items-selected?
  ; @param (keyword) browser-id
  ;
  ; @usage
  ;  (r no-items-selected? db :my-browser)
  ;
  ; @return (boolean)
  [db [_ browser-id]]
  (r selection.subs/no-items-selected? db browser-id))

(defn item-selected?
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-selected? db :my-browser "my-item")
  ;
  ; @return (boolean)
  [db [_ browser-id item-id]]
  (r selection.subs/item-selected? db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/get-selected-item-count :my-browser]
(r/reg-sub :item-browser/get-selected-item-count get-selected-item-count)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/all-items-selected? :my-browser]
(r/reg-sub :item-browser/all-items-selected? all-items-selected?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/all-downloaded-items-selected? :my-browser]
(r/reg-sub :item-browser/all-downloaded-items-selected? all-downloaded-items-selected?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/any-item-selected? :my-browser]
(r/reg-sub :item-browser/any-item-selected? any-item-selected?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/any-downloaded-item-selected? :my-browser]
(r/reg-sub :item-browser/any-downloaded-item-selected? any-downloaded-item-selected?)

; @param (keyword) browser-id
;
; @usage
;  [:item-browser/no-items-selected? :my-browser]
(r/reg-sub :item-browser/no-items-selected? no-items-selected?)

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
;  [:item-browser/item-selected? :my-browser "my-item"]
(r/reg-sub :item-browser/item-selected? item-selected?)
