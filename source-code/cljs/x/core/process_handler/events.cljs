
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.process-handler.events
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-process-progress!
  ; @param (keyword) process-id
  ; @param (percent) process-progress
  ;
  ; @usage
  ;  (r set-process-progress! db :my-process 100)
  ;
  ; @return (map)
  [db [_ process-id process-progress]]
  (assoc-in db [:x.core :process-handler/data-items process-id :progress] process-progress))

(defn set-process-status!
  ; @param (keyword) process-id
  ; @param (keyword) process-status
  ;  :prepare, :progress, :failure, :success
  ;
  ; @usage
  ;  (r set-process-status! db :my-process :success)
  ;
  ; @return (map)
  [db [_ process-id process-status]]
  (assoc-in db [:x.core :process-handler/data-items process-id :status] process-status))

(defn set-process-activity!
  ; @param (keyword) process-id
  ; @param (keyword) process-activity
  ;  :active, :idle, :stalled, ...
  ;
  ; @usage
  ;  (r set-process-activity! db :my-process :active)
  ;
  ; @return (map)
  [db [_ process-id process-activity]]
  (assoc-in db [:x.core :process-handler/data-items process-id :activity] process-activity))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.core/set-process-progress! :my-process 100]
(r/reg-event-db :x.core/set-process-progress! set-process-progress!)

; @usage
;  [:x.core/set-process-status! :my-process :success]
(r/reg-event-db :x.core/set-process-status! set-process-status!)

; @usage
;  [:x.core/set-process-activity! :my-process :active]
(r/reg-event-db :x.core/set-process-activity! set-process-activity!)
