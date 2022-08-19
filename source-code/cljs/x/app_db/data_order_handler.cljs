
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5




;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.data-order-handler
    (:require [mid-fruits.vector           :as vector]
              [x.app-core.api              :as a :refer [r]]
              [x.app-db.engine             :as engine]
              [x.mid-db.data-order-handler :as data-order-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-order-handler
(def data-item-last?          data-order-handler/data-item-last?)
(def data-item-first?         data-order-handler/data-item-first?)
(def get-data-item-position   data-order-handler/get-data-item-position)
(def move-data-item-to-last!  data-order-handler/move-data-item-to-last!)
(def move-data-item-to-first! data-order-handler/move-data-item-to-first!)
(def move-data-item!          data-order-handler/move-data-item!)
(def remove-data-item!        data-order-handler/remove-data-item!)
(def add-data-item!           data-order-handler/add-data-item!)
(def update-data-item!        data-order-handler/update-data-item!)
(def apply-data-item!         data-order-handler/apply-data-item!)
(def apply-data-items!        data-order-handler/apply-data-items!)
(def copy-item-to-partition!  data-order-handler/copy-item-to-partition!)
(def move-item-to-partition!  data-order-handler/move-item-to-partition!)
(def empty-partition!         data-order-handler/empty-partition!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-db :db/move-data-item-to-last!  move-data-item-to-last!)
(a/reg-event-db :db/move-data-item-to-first! move-data-item-to-first!)
(a/reg-event-db :db/move-data-item!          move-data-item!)
(a/reg-event-db :db/remove-data-item!        remove-data-item!)
(a/reg-event-db :db/add-data-item!           add-data-item!)
(a/reg-event-db :db/update-data-item!        update-data-item!)
(a/reg-event-db :db/apply-data-item!         apply-data-item!)
(a/reg-event-db :db/empty-partition!         empty-partition!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-data-item-id!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/remove-data-item-id! db :my-partition :my-item-id)
  [db [_ partition-id data-item-id]]
  (r engine/apply-item! db [partition-id :data-order] vector/remove-item data-item-id))

; @usage
;  [:db/remove-data-item-id! db :my-partition :my-item-id]
(a/reg-event-db :db/remove-data-item-id! remove-data-item-id!)

(defn remove-data-item-props!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  (r db/remove-data-item-props! db :my-partition :my-item-id)
  [db [_ partition-id data-item-id]]
  (r engine/remove-item! db [partition-id :data-items data-item-id]))

; @usage
;  [:db/remove-data-item-props! db :my-partition :my-item-id]
(a/reg-event-db :db/remove-data-item-props! remove-data-item-props!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :db/remove-data-item-later!
  ; Bizonyos esetekben szükséges a data-item azonosítóját hamarabb eltávolítani,
  ; mint a data-item által tárolt adatokat.
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  ;
  ; @usage
  ;  [:db/remove-data-item-later! :my-partition :my-item-id]
  (fn [{:keys [db]} [_ partition-id data-item-id]]
      {:dispatch-later
        ; 1.  0ms
       [{:ms  0 :dispatch [:db/remove-data-item-id!    partition-id data-item-id]}
        ; 2. 50ms
        {:ms 50 :dispatch [:db/remove-data-item-props! partition-id data-item-id]}]}))
