
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.16
; Description:
; Version: v0.3.8
; Compatibility: x4.5.2



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
  (get-in db (db/path :gestures/view-handlers handler-id :view-id)))

; @usage
;  [:gestures/get-selected-view-id]
(a/reg-sub :gestures/get-selected-view-id get-selected-view-id)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-view-handler!
  ; @param (keyword) handler-id
  ; @param (map) handler-props
  ;  {:default-view-id (keyword)}
  ;
  ; @usage
  ;  (r gestures/init-view-handler! db :my-view-handler {:default-view-id :my-view})
  ;
  ; @return (map)
  [db [_ handler-id {:keys [default-view-id]}]]
  (assoc-in db (db/path :gestures/view-handlers handler-id :view-id) default-view-id))

; @usage
;  [:gestures/init-view-handler! :my-view-handler {:default-view-id :my-view}]
(a/reg-event-db :gestures/init-view-handler! init-view-handler!)

(defn change-view!
  ; @param (keyword) handler-id
  ; @param (keyword) view-id
  ;
  ; @usage
  ;  (r gestures/change-view! db :my-view-handler :my-view)
  ;
  ; @return (map)
  [db [_ handler-id view-id]]
  (assoc-in db (db/path :gestures/view-handlers handler-id :view-id) view-id))

; @usage
;  [:gestures/change-view! :my-view-handler :my-view]
(a/reg-event-db :gestures/change-view! change-view!)
