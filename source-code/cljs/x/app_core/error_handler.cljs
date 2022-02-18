;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.6.2
; Compatibility: x4.6.1



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
  (if error (str error)
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



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-warning!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) warning-message
  [& warning-message]
  (.warn js/console (reduce #(str %1 "\n" %2) nil warning-message)))

(event-handler/reg-fx :core/print-warning! print-warning!)

(defn print-error!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) error-message
  [& error-message]
  (.error js/console (reduce #(str %1 "\n" %2) nil error-message)))

(event-handler/reg-fx :core/print-error! print-error!)



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
      (let [error-message (r get-error-message db error-id error-props)
            catched-event (-> error-props :cofx event-handler/cofx->event-vector)]
           {:db (r load-handler/stop-synchronizing! db)
            :fx [:core/print-error! error-message catched-event]
            :dispatch [:ui/set-shield! {:content error-message}]})))
