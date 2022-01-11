
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.6.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.select-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [mid-fruits.vector :as vector]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- handler-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) handler-props
  ;  {:enabled? (boolean)(opt)
  ;   :max-selected-count (integer)(opt)
  ;   :selected-items (vector)(opt)}
  ;
  ; @return (map)
  ;  {:enabled? (boolean)
  ;   :max-selected-count (integer)
  ;   :selected-items (vector)}
  [handler-props]
  (merge {:enabled?           true
          :max-selected-count 256
          :selected-items     []}
         (param handler-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (get-in db (db/path :gestures/select-handlers handler-id :selected-items)))

(defn select-handler-nonempty?
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [get-selected-item-ids (r get-selected-item-ids db handler-id)]
       (vector/nonempty? get-selected-item-ids)))

(defn select-handler-empty?
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [get-selected-item-ids (r get-selected-item-ids db handler-id)]
       (-> get-selected-item-ids vector/nonempty? not)))

(defn select-handler-enabled?
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [handler-enabled? (get-in db (db/path :gestures/select-handlers handler-id :enabled?))]
       (boolean handler-enabled?)))

(defn select-handler-disabled?
  ; @param (keyword) handler-id
  ;
  ; @return (vector)
  [db [_ handler-id]]
  (let [handler-enabled? (get-in db (db/path :gestures/select-handlers handler-id :enabled?))]
       (not handler-enabled?)))

(defn item-selected?
  ; @param (keyword) handler-id
  ; @param (keyword) item-id
  ;
  ; @return (boolean)
  [db [_ handler-id item-id]]
  (let [selected-item-ids (r get-selected-item-ids db handler-id)]
       (vector/contains-item? selected-item-ids item-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-select-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:enabled? (boolean)(opt)
  ;    Default: true
  ;   :max-selected-count (integer)(opt)
  ;    Default: 256
  ;   :selected-items (vector)(opt)
  ;    Default: []}
  ;
  ; @return (map)
  [db [_ handler-id handler-props]]
  (let [handler-props (handler-props-prototype handler-props)]
       (assoc-in db (db/path :gestures/select-handlers handler-id) handler-props)))

(a/reg-event-db :gestures/init-select-handler! init-select-handler!)

(defn empty-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db (db/path :gestures/select-handlers handler-id :selected-items)
               (param [])))

(a/reg-event-db :gestures/empty-select-handler! empty-select-handler!)

(defn enable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db (db/path :gestures/select-handlers handler-id :enabled?) true))

(a/reg-event-db :gestures/enable-select-handler! enable-select-handler!)

(defn disable-select-handler!
  ; @param (keyword) handler-id
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db (db/path :gestures/select-handlers handler-id :enabled?) false))

(a/reg-event-db :gestures/disable-select-handler! disable-select-handler!)

(defn select-item!
  ; @param (keyword) handler-id
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [db [_ handler-id item-id]]
  (update-in db (db/path :gestures/select-handlers handler-id :selected-items)
             vector/conj-item item-id))

(a/reg-event-db :gestures/select-item! select-item!)
