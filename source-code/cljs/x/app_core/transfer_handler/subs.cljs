
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.transfer-handler.subs
    (:require [x.app-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-transfer-data
  ; @param (keyword) transfer-id
  ;
  ; @usage
  ;  (r a/get-transfer-data db :my-transfer)
  ;
  ; @return (*)
  [db [_ transfer-id]]
  (get-in db [:core :transfer-handler/data-items transfer-id]))

(defn get-transfer-item
  ; @param (keyword) transfer-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-transfer-item db :my-transfer :my-item)
  ;
  ; @return (*)
  [db [_ transfer-id item-key]]
  (get-in db [:core :transfer-handler/data-items transfer-id item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-transfer-data :my-transfer]
;(event-handler/reg-sub :core/get-transfer-data get-transfer-data)

; @usage
;  [:core/get-transfer-data :my-transfer :my-item]
;(event-handler/reg-sub :core/get-transfer-item get-transfer-item)
