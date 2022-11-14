
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.error-handler.effects
    (:require [re-frame.api               :as r :refer [r]]
              [x.core.error-handler.subs  :as error-handler.subs]
              [x.core.load-handler.events :as load-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.core/error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) error-id
  ; @param (map)(opt) error-props
  ;  {:cofx (map)(opt)
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
  ;                        :cofx  {...}}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ error-id {:keys [cofx] :as error-props}]]
      (let [error-message (r error-handler.subs/get-error-message db error-id error-props)
            catched-event (r/cofx->event-vector cofx)]
           {:db   (r load-handler.events/stop-loading! db)
            :fx-n [[:x.core/print-error!    error-message catched-event]
                   [:x.ui/set-error-shield! error-message]]})))
