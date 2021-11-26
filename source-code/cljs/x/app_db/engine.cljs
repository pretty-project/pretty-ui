
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.08
; Description:
; Version: v1.0.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a]
              [x.mid-db.engine    :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.engine
(def item-path->cofx-path engine/item-path->cofx-path)
(def get-db               engine/get-db)
(def get-item             engine/get-item)
(def item-exists?         engine/item-exists?)
(def empty-db!            engine/empty-db!)
(def move-item!           engine/move-item!)
(def set-item!            engine/set-item!)
(def set-vector-item!     engine/set-vector-item!)
(def remove-item!         engine/remove-item!)
(def remove-vector-item!  engine/remove-vector-item!)
(def remove-item-n!       engine/remove-item-n!)
(def inc-item-n!          engine/inc-item-n!)
(def dec-item-n!          engine/dec-item-n!)
(def apply!               engine/apply!)
(def distribute-items!    engine/distribute-items!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-db]
(a/reg-sub :db/get-db get-db)

; @usage
;  [:db/get-item [:my :item :path]]
(a/reg-sub :db/get-item get-item)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/empty-db!]
(a/reg-event-db :db/empty-db! empty-db!)

; @usage
;  [:db/move-item! [:move :from :path] [:move :to :path]]
(a/reg-event-db :db/move-item! move-item!)

; @usage
;  [:db/set-item! [:my :item :path] "My value"]
(a/reg-event-db :db/set-item! set-item!)

; @usage
;  [:db/set-vector-item! [:my :item :path :0] "My value"]
(a/reg-event-db :db/set-vector-item! set-vector-item!)

; @usage
;  [:db/remove-item! [:my :item :path]]
(a/reg-event-db :db/remove-item! remove-item!)

; @usage
;  [:db/remove-vector-item! [:my :item :path 0]]
(a/reg-event-db :db/remove-vector-item! remove-vector-item!)

; @usage
;  [:db/remove-item-n! [[:my :item :path] [...]]]
(a/reg-event-db :db/remove-item-n! remove-item-n!)

; @usage
;  [:db/inc-item-n! [[:my :item :path] [...]]]
(a/reg-event-db :db/inc-item-n! inc-item-n!)

; @usage
;  [:db/dec-item-n! [[:my :item :path] [...]]]
(a/reg-event-db :db/dec-item-n! dec-item-n!)

; @usage
;  [:db/apply! [:my :item :path] merge {}]
(a/reg-event-db :db/apply! apply!)

; @usage
;  [:db/distribute-items! {:apple "red" :banana "yellow"}]
;                         {:apple  [:where :to :store :apples :color]
;                          :banana [:where :to :store :bananas :color]}]
(a/reg-event-db :db/distribute-items! distribute-items!)



;; -- XXX#1071 ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn db-event-handler
  ; @param (vectors in vector) event-vectors
  [event-vectors]
  (doseq [event-vector event-vectors]
         (let [event-id            (first event-vector)
               namespaced-event-id (keyword/add-namespace :x.app-db event-id)
               event-vector (vector/change-nth-item event-vector 0 namespaced-event-id)]
              (a/dispatch event-vector))))

; @usage
;  {:dispatch [...]
;   :x.app-db [[:set-item! [ *item-path* ] "new-value"]
;              [:apply!    [ *item-path* ] merge {}]]}
(a/reg-fx :x.app-db db-event-handler)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :db/travel-db-in-time!
  ; @param (ms) travel-duration
  (fn [{:keys [db]} [_ travel-duration]]
      {:dispatch-later [{:ms travel-duration :dispatch {:db db}}]}))
