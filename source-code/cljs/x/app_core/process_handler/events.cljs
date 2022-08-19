
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.process-handler.events
    (:require [x.app-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-process-progress!
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ;
  ; @usage
  ;  (r a/set-process-progress! db :my-process 100)
  ;
  ; @return (map)
  [db [_ process-id process-progress]]
  (assoc-in db [:core :process-handler/data-items process-id :progress] process-progress))

(defn set-process-status!
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ;  :prepare, :progress, :failure, :success
  ;
  ; @usage
  ;  (r a/set-process-status! db :my-process :success)
  ;
  ; @return (map)
  [db [_ process-id process-status]]
  (assoc-in db [:core :process-handler/data-items process-id :status] process-status))

(defn set-process-activity!
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ;  :active, :idle, :stalled, ...
  ;
  ; @usage
  ;  (r a/set-process-activity! db :my-process :active)
  ;
  ; @return (map)
  [db [_ process-id process-activity]]
  (assoc-in db [:core :process-handler/data-items process-id :activity] process-activity))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/set-process-progress! :my-process 100]
(event-handler/reg-event-db :core/set-process-progress! set-process-progress!)

; @usage
;  [:core/set-process-status! :my-process :success]
(event-handler/reg-event-db :core/set-process-status! set-process-status!)

; @usage
;  [:core/set-process-activity! :my-process :active]
(event-handler/reg-event-db :core/set-process-activity! set-process-activity!)
