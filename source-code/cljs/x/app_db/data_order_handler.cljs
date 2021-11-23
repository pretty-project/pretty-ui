
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.data-order-handler
    (:require [mid-fruits.vector           :as vector]
              [x.app-core.api              :as a :refer [r]]
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
(def update-data-order!       data-order-handler/update-data-order!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-db :x.app-db/move-data-item-to-last!  move-data-item-to-last!)
(a/reg-event-db :x.app-db/move-data-item-to-first! move-data-item-to-first!)
(a/reg-event-db :x.app-db/move-data-item!          move-data-item!)
(a/reg-event-db :x.app-db/remove-data-item!        remove-data-item!)
(a/reg-event-db :x.app-db/add-data-item!           add-data-item!)
(a/reg-event-db :x.app-db/update-data-item!        update-data-item!)
(a/reg-event-db :x.app-db/apply-data-item!         apply-data-item!)
(a/reg-event-db :x.app-db/empty-partition!         empty-partition!)
(a/reg-event-db :x.app-db/update-data-order!       update-data-order!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-db/remove-data-item-id!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  (fn [_ [_ partition-id data-item-id]]
      [:x.app-db/apply! [partition-id :data-order] vector/remove-item data-item-id]))

(a/reg-event-fx
  :x.app-db/remove-data-item-props!
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  (fn [_ [_ partition-id data-item-id]]
      [:x.app-db/remove-item! [partition-id :data-items data-item-id]]))

(a/reg-event-fx
  :x.app-db/remove-data-item-later!
  ; Bizonyos esetekben szükséges a data-item azonosítóját hamarabb eltávolítani,
  ; mint a data-item által tárolt adatokat.
  ;
  ; @param (namespaced keyword) partition-id
  ; @param (keyword) data-item-id
  (fn [{:keys [db]} [_ partition-id data-item-id]]
      {:dispatch-later
        ; 1.  0ms
       [{:ms  0 :dispatch [:x.app-db/remove-data-item-id!    partition-id data-item-id]}
        ; 2. 50ms
        {:ms 50 :dispatch [:x.app-db/remove-data-item-props! partition-id data-item-id]}]}))
