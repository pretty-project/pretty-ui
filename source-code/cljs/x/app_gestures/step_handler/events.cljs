
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-gestures.step-handler.events
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pause-stepping!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/pause-stepping! db :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:gestures :step-handler/data-items handler-id :paused?] true))

(defn run-stepping!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r gestures/run-stepping! db :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:gestures :step-handler/data-items handler-id :paused?] false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:gestures/pause-stepping! :my-handler]
(a/reg-event-db :gestures/pause-stepping! pause-stepping!)

; @usage
;  [:gestures/run-stepping! :my-handler]
(a/reg-event-db :gestures/run-stepping! run-stepping!)
