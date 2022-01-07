;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.4.8
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
  ;
  ; @return (string)
  [db [_ error-id]]
  (get-in db [::errors :data-items error-id :error]
             (return DEFAULT-APPLICATION-ERROR)))

(defn- get-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ;
  ; @return (string)
  [db [_ error-id]]
  (if (r debug-handler/debug-mode-detected? db)
      (r get-developer-error-message        db error-id)
      (return DEFAULT-APPLICATION-ERROR)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-error-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ; @param (map) error-props
  ;  {:event-id (keyword)
  ;   :error (string)}
  ;
  ; @return (map)
  [db [_ error-id error-props]]
  (assoc-in db [::errors :data-items error-id] error-props))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/->error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) error-id
  ; @param (map) error-props
  ;  {:event-id (keyword)
  ;   :error (string)}
  (fn [{:keys [db]} event-vector]
      (let [error-id    (event-handler/event-vector->second-id   event-vector)
            error-props (event-handler/event-vector->first-props event-vector)]
           {:db (as-> db % (r store-error-props!               % error-id error-props)
                           (r load-handler/stop-synchronizing! %))
            :dispatch [:ui/set-shield! {:content (r get-error-message db error-id)}]})))
