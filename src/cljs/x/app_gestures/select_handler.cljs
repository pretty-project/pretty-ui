
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.23
; Description:
; Version: v0.4.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.select-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [mid-fruits.vector :as vector]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) selector-props
  ;  {:enabled? (boolean)(opt)
  ;   :max-selected-count (integer)(opt)
  ;   :selected-items (vector)(opt)}
  ;
  ; @return (map)
  ;  {:enabled? (boolean)
  ;   :max-selected-count (integer)
  ;   :selected-items (vector)}
  [selector-props]
  (merge {:enabled?           true
          :max-selected-count 256
          :selected-items     []}
         (param selector-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-ids
  ; @param (keyword) selector-id
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (get-in db (db/path ::selectors selector-id :selected-items)))

(defn selector-nonempty?
  ; @param (keyword) selector-id
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (vector/nonempty? (r get-selected-item-ids db selector-id)))

(defn selector-empty?
  ; @param (keyword) selector-id
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (not (r selector-nonempty? db selector-id)))

(defn selector-enabled?
  ; @param (keyword) selector-id
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (boolean (get-in db (db/path ::selectors selector-id :enabled?))))

(defn selector-disabled?
  ; @param (keyword) selector-id
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (not (r selector-enabled? db selector-id)))

(defn item-selected?
  ; @param (keyword) selector-id
  ; @param (keyword) item-id
  ;
  ; @return (boolean)
  [db [_ selector-id item-id]]
  (let [selected-item-ids (r get-selected-item-ids db selector-id)]
       (vector/contains-item? selected-item-ids item-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-selector!
  ; @param (keyword) selector-id
  ; @param (map)(opt) selector-props
  ;  {:enabled? (boolean)(opt)
  ;    Default: true
  ;   :max-selected-count (integer)(opt)
  ;    Default: 256
  ;   :selected-items (vector)(opt)
  ;    Default: []}
  ;
  ; @return (map)
  [db [_ selector-id selector-props]]
  (let [selector-props (a/prot selector-props selector-props-prototype)]
       (assoc-in db (db/path ::selectors selector-id) selector-props)))

(a/reg-event-db :x.app-gestures/init-selector! init-selector!)

(defn empty-selector!
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (assoc-in db (db/path ::selectors selector-id :selected-items)
               (param [])))

(a/reg-event-db :x.app-gestures/empty-selector! empty-selector!)

(defn enable-selector!
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (assoc-in db (db/path ::selectors selector-id :enabled?) true))

(a/reg-event-db :x.app-gestures/enable-selector! enable-selector!)

(defn disable-selector!
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (assoc-in db (db/path ::selectors selector-id :enabled?) false))

(a/reg-event-db :x.app-gestures/disable-selector! disable-selector!)

(defn select-item!
  ; @param (keyword) selector-id
  ; @param (keyword) item-id
  ;
  ; @return (map)
  [db [_ selector-id item-id]]
  (update-in db (db/path ::selectors selector-id :selected-items)
                vector/conj-item item-id))

(a/reg-event-db :x.app-gestures/select-item! select-item!)
