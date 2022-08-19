
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler.effects
    (:require [x.server-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) error-id
  ; @param (map)(opt) error-props
  ;  {:cofx (map)(opt)
  ;   :error (string)(opt)}
  ;
  ; @usage
  ;  [:core/error-catched]
  ;
  ; @usage
  ;  [:core/error-catched {...}]
  ;
  ; @usage
  ;  [:core/error-catched :my-error {...}]
  ;
  ; @usage
  ;  [:core/error-catched {:error "An error occured ..."
  ;                        :cofx  {...}}]
  [event-handler/event-vector<-id]
  (fn [{:keys [db]} [_ error-id {:keys [cofx error]}]]
      (let [catched-event (event-handler/cofx->event-vector cofx)]
           {:fx [:core/print-error! error]})))
