;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.5.6
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.error-handler
    (:require [mid-fruits.candy         :refer [param return]]
              [x.app-core.debug-handler :as debug-handler]
              [x.app-core.engine        :as engine]
              [x.app-core.event-handler :as event-handler :refer [r]]
              [x.app-core.load-handler  :as load-handler]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-APPLICATION-ERROR "Something went wrong :(")



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-developer-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ; @param (map) error-props
  ;  {:error (string)(opt)}
  ;
  ; @return (string)
  [_ [_ _ {:keys [error]}]]
  (if (some? error)
      (str error)
      (str DEFAULT-APPLICATION-ERROR)))

(defn- get-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ; @param (map) error-props
  ;
  ; @return (string)
  [db [_ error-id error-props]]
  (if (r debug-handler/debug-mode-detected? db)
      (r get-developer-error-message        db error-id error-props)
      (return DEFAULT-APPLICATION-ERROR)))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/->error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) error-id
  ; @param (map)(opt) error-props
  ;  {:cofx (map)(opt)
  ;   :error (string)(opt)}
  ;
  ; @usage
  ;  [:core/->error-catched]
  ;
  ; @usage
  ;  [:core/->error-catched {...}]
  ;
  ; @usage
  ;  [:core/->error-catched :my-error {...}]
  ;
  ; @usage
  ;  [:core/->error-catched {:error "An error occured ..."
  ;                          :cofx  {...}}]
  (fn [{:keys [db]} event-vector]
      (let [error-id      (event-handler/event-vector->second-id   event-vector)
            error-props   (event-handler/event-vector->first-props event-vector)
            error-message (r get-error-message db error-id error-props)
            catched-event (-> error-props :cofx event-handler/cofx->event-vector)]
           (.error js/console (str error-message "\n" catched-event))
           {:db (r load-handler/stop-synchronizing! db)
            :dispatch [:ui/set-shield! {:content error-message}]})))
