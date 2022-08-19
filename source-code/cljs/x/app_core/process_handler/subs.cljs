

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.process-handler.subs
    (:require [x.app-core.event-handler :refer [r]]))



;; -- Process status subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-status
  ; @param (keyword) process-id
  ;
  ; @return (keyword)
  [db [_ process-id]]
  (let [process-status (get-in db [:core :process-handler/data-items process-id :status])]
       (or process-status :ready)))

(defn process-ready?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :ready)))

(defn process-preparing?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :prepare)))

(defn process-in-progress?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :progress)))

(defn process-success?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :success)))

(defn process-failured?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :failure)))

(defn process-blocked?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-status (r get-process-status db process-id)]
       (= process-status :blocked)))



;; -- Process activity subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-activity
  ; @param (keyword) process-id
  ;
  ; @return (keyword)
  [db [_ process-id]]
  (let [process-activity (get-in db [:core :process-handler/data-items process-id :activity])]
       (or process-activity :inactive)))

(defn process-inactive?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :inactive)))

(defn process-active?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :active)))

(defn process-idle?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :idle)))

(defn process-stalled?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (let [process-activity (r get-process-activity db process-id)]
       (= process-activity :stalled)))



;; -- Process progress subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-process-progress
  ; @param (keyword) process-id
  ;
  ; @return (percent)
  [db [_ process-id]]
  (let [process-progress (get-in db [:core :process-handler/data-items process-id :progress])]
       (or process-progress 0)))

(defn process-done?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (= 100 (r get-process-progress db process-id)))



;; -- Process control subscriptions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-process?
  ; @param (keyword) process-id
  ;
  ; @return (boolean)
  [db [_ process-id]]
  (not (or (r process-active?  db process-id)
           (r process-blocked? db process-id))))
