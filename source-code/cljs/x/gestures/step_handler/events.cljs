
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.gestures.step-handler.events
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pause-stepping!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r pause-stepping! db :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:x.gestures :step-handler/data-items handler-id :paused?] true))

(defn run-stepping!
  ; @param (keyword) handler-id
  ;
  ; @usage
  ;  (r run-stepping! db :my-handler)
  ;
  ; @return (map)
  [db [_ handler-id]]
  (assoc-in db [:x.gestures :step-handler/data-items handler-id :paused?] false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.gestures/pause-stepping! :my-handler]
(r/reg-event-db :x.gestures/pause-stepping! pause-stepping!)

; @usage
;  [:x.gestures/run-stepping! :my-handler]
(r/reg-event-db :x.gestures/run-stepping! run-stepping!)
