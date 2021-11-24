
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.16
; Description:
; Version: v0.2.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.view-handler
    (:require [mid-fruits.candy  :refer [param]]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [mid-fruits.vector :as vector]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-view-id
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/get-selected-view-id db :my-view-handler)
  ;
  ; @return (keyword)
  [db [_ handler-id]]
  (get-in db (db/path ::view-handlers handler-id :view-id)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-view-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:default-view (keyword)}
  ;
  ; @return (map)
  [db [_ handler-id {:keys [default-view]}]]
  (assoc-in db (db/path ::view-handlers handler-id :view-id) default-view))

(a/reg-event-db :gestures/init-view-handler! init-view-handler!)

(defn change-view!
  ; @param (keyword) handler-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ handler-id view-id]]
  (assoc-in db (db/path ::view-handlers handler-id :view-id) view-id))

(a/reg-event-db :gestures/change-view! change-view!)
