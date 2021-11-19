
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v2.6.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.position-handler
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-id->element-position
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (integer)
  [element-id]
  (let [element (dom/get-element-by-id (keyword/to-dom-value element-id))]
       (dom/get-element-relative-position element)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-element-position
  ; @param (keyword) element-id
  ;
  ; @return (integer)
  [db [_ element-id]]
  (get-in db (db/path ::element-positions element-id)))

(a/reg-sub ::get-element-position get-element-position)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-position-listener!
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (assoc-in db (db/path ::element-positions element-id)
               (element-id->element-position element-id)))

(a/reg-event-db ::add-position-listener! add-position-listener!)

(defn- remove-position-listener!
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (r db/remove-item! db (db/path ::element-positions element-id)))

(a/reg-event-db ::remove-position-listener! remove-position-listener!)

(defn- update-stored-positions!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (if-let [stored-positions (get-in db (db/path ::element-positions))]
          (assoc-in db (db/path ::element-positions)
                       (reduce-kv #(assoc %1 %2 (element-id->element-position %2))
                                   (param {})
                                   (param stored-positions)))
          (return db)))

(a/reg-event-db ::update-stored-positions! update-stored-positions!)
