
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.selection.subs
    (:require [engines.engine-handler.selection.subs :as selection.subs]
              [re-frame.api                          :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (r export-selection db :my-lister)
  ;  =>
  ;  ["my-item" "your-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id]]
  (r selection.subs/export-selection db lister-id))

(defn export-single-selection
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (r export-single-selection db :my-lister)
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ lister-id]]
  (r selection.subs/export-single-selection db lister-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r get-selected-item-count db :my-lister)
  ;
  ; @return (integer)
  [db [_ lister-id]]
  (r selection.subs/get-selected-item-count db lister-id))

(defn all-items-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r all-items-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/all-items-selected? db lister-id))

(defn all-downloaded-items-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r all-downloaded-items-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/all-downloaded-items-selected? db lister-id))

(defn any-item-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r any-item-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/any-item-selected? db lister-id))

(defn any-downloaded-item-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r any-downloaded-item-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/any-downloaded-item-selected? db lister-id))

(defn no-items-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r no-items-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/no-items-selected? db lister-id))

(defn item-selected?
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-selected? db :my-lister "my-item")
  ;
  ; @return (boolean)
  [db [_ lister-id item-id]]
  (r selection.subs/item-selected? db lister-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/get-selected-item-count :my-lister]
(r/reg-sub :item-lister/get-selected-item-count get-selected-item-count)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/all-items-selected? :my-lister]
(r/reg-sub :item-lister/all-items-selected? all-items-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/all-downloaded-items-selected? :my-lister]
(r/reg-sub :item-lister/all-downloaded-items-selected? all-downloaded-items-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/any-item-selected? :my-lister]
(r/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/any-downloaded-item-selected? :my-lister]
(r/reg-sub :item-lister/any-downloaded-item-selected? any-downloaded-item-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/no-items-selected? :my-lister]
(r/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
;  [:item-lister/item-selected? :my-lister "my-item"]
(r/reg-sub :item-lister/item-selected? item-selected?)
