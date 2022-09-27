
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.items.subs
    (:require [plugins.plugin-handler.items.subs :as items.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/get-item db :my-lister "my-item")
  ;
  ; @return (map)
  [db [_ lister-id item-id]]
  (r items.subs/get-item db lister-id item-id))

(defn export-item
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/export-item db :my-lister "my-item")
  ;
  ; @return (namespaced map)
  [db [_ lister-id item-id]]
  (r items.subs/export-item db lister-id item-id))

(defn item-downloaded?
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/item-downloaded? db :my-lister "my-item")
  ;
  ; @return (boolean)
  [db [_ lister-id item-id]]
  (r items.subs/item-downloaded? db lister-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-dex
  ; @param (keyword) lister-id
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-lister/get-item-dex db :my-lister "my-item")
  ;
  ; @return (integer)
  [db [_ lister-id item-id]]
  (r items.subs/get-item-dex db lister-id item-id))

(defn get-item-dexes
  ; @param (keyword) lister-id
  ; @param (string) item-ids
  ;
  ; @usage
  ;  (r item-lister/get-item-dexes db :my-lister ["my-item"])
  ;
  ; @return (integers in vector)
  [db [_ lister-id item-ids]]
  (r items.subs/get-item-dexes db lister-id item-ids))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-disabled?
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/item-disabled? db :my-lister 42)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (r items.subs/item-disabled? db lister-id item-dex))

(defn item-last?
  ; @param (keyword) lister-id
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/item-last? db :my-lister 42)
  ;
  ; @return (boolean)
  [db [_ lister-id item-dex]]
  (r items.subs/item-last? db lister-id item-dex))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
; @param (string) item-id
;
; @usage
;  [:item-lister/get-item :my-lister "my-item"]
(a/reg-sub :item-lister/get-item get-item)

; @param (keyword) lister-id
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-last? :my-lister 42]
(a/reg-sub :item-lister/item-last? item-last?)
