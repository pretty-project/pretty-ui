;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.06
; Description:
; Version: v0.4.4
; Compatibility: x4.4.6



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
  ; @return (string)
  [db _]
  (param DEFAULT-APPLICATION-ERROR))

(defn- get-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (if (debug-handler/debug-mode?)
      (r get-developer-error-message db)
      (return DEFAULT-APPLICATION-ERROR)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-error-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) error-props
  ;
  ; @return (map)
  [db [_ error-props]]
  (assoc-in db [::errors :data-items (engine/id)] error-props))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(event-handler/reg-event-fx
  :core/->error-catched
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-props
  (fn [{:keys [db]} [event-id error-props]]
      {:db       (-> db (store-error-props!               [event-id error-props])
                        (load-handler/stop-synchronizing! [event-id]))
       :dispatch [:ui/set-shield! {:content (r get-error-message db)}]}))
