
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.error-handler.effects
    (:require [x.app-core.error-handler.subs :as error-handler.subs]
              [x.app-core.event-handler      :as event-handler :refer [r]]
              [x.app-core.load-handler       :as load-handler]))



;; -- Effect events -----------------------------------------------------------
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
  (fn [{:keys [db]} [_ error-id error-props]]
      (let [error-message (r error-handler.subs/get-error-message db error-id error-props)
            catched-event (-> error-props :cofx event-handler/cofx->event-vector)]
           {:db (r load-handler/stop-synchronizing! db)
            :fx [:core/print-error! error-message catched-event]
            :dispatch [:ui/set-shield! {:content error-message}]})))
