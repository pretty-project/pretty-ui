
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.selection.subs
    (:require [plugins.plugin-handler.selection.subs :as selection.subs]
              [x.app-core.api                        :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-count
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/get-selected-item-count db :my-lister)
  ;
  ; @return (integer)
  [db [_ lister-id]]
  (r selection.subs/get-selected-item-count db lister-id))

(defn all-items-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/all-items-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/all-items-selected? db lister-id))

(defn any-item-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/any-item-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/any-item-selected? db lister-id))

(defn no-items-selected?
  ; @param (keyword) lister-id
  ;
  ; @usage
  ;  (r item-lister/no-items-selected? db :my-lister)
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r selection.subs/no-items-selected? db lister-id))

(defn item-selected?
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/item-selected? db :my-lister 42)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (r selection.subs/item-selected? db lister-id item-dex))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-selection
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (r item-lister/export-selection db :my-lister)
  ;  =>
  ;  ["my-item" "your-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id]]
  (r selection.subs/export-selection db lister-id))

(defn get-imported-selection
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (r item-lister/get-imported-selection db :my-lister)
  ;  =>
  ;  ["my-item" "your-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id]]
  (r selection.subs/get-imported-selection db lister-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/get-selected-item-count :my-lister]
(a/reg-sub :item-lister/get-selected-item-count get-selected-item-count)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/all-items-selected? :my-lister]
(a/reg-sub :item-lister/all-items-selected? all-items-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/any-item-selected? :my-lister]
(a/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/no-items-selected? :my-lister]
(a/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-selected? :my-lister 42]
(a/reg-sub :item-lister/item-selected? item-selected?)
