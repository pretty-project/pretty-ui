
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.error-handler.effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) error-id
  ; @param (map)(opt) error-props
  ;  {:event (vector)(opt)
  ;   :error (string)(opt)}
  ;
  ; @usage
  ;  [:x.core/error-catched]
  ;
  ; @usage
  ;  [:x.core/error-catched {...}]
  ;
  ; @usage
  ;  [:x.core/error-catched :my-error {...}]
  ;
  ; @usage
  ;  [:x.core/error-catched {:error "An error occured ..."
  ;                          :event [...]}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ error-id {:keys [error event]}]]
      {:fx [:x.core/print-error! error]}))
