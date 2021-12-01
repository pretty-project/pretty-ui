
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v2.6.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.position-handler
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
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
  (let [element (dom/get-element-by-id (a/dom-value element-id))]
       (dom/get-element-relative-position element)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-position
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (r environment/get-element-position db :my-element)
  ;
  ; @return (integer)
  [db [_ element-id]]
  (get-in db (db/path ::element-positions element-id)))

; @usage
;  [:environment/get-element-position :my-element]
(a/reg-sub :environment/get-element-position get-element-position)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-element-position-listener!
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (r environment/add-element-position-listener! db :my-element)
  ;
  ; @return (map)
  [db [_ element-id]]
  (assoc-in db (db/path ::element-positions element-id)
               (element-id->element-position element-id)))

; @usage
;  [:environment/add-element-position-listener! :my-element]
(a/reg-event-db :environment/add-element-position-listener! add-element-position-listener!)

(defn remove-element-position-listener!
  ; @param (keyword) element-id
  ;
  ; @usage
  ;  (r environment/remove-element-position-listener! db :my-element)
  ;
  ; @return (map)
  [db [_ element-id]]
  (r db/remove-item! db (db/path ::element-positions element-id)))

; @usage
;  [:environment/remove-element-position-listener! :my-element]
(a/reg-event-db :environment/remove-element-position-listener! remove-element-position-listener!)

(defn- update-stored-element-positions!
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

(a/reg-event-db :environment/update-stored-element-positions! update-stored-element-positions!)
