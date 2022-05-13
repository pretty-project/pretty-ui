
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.events
    (:require [x.server-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-server-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-props
  ;
  ; @return (map)
  [db [_ server-props]]
  (assoc-in db [:core :server-handler/server-props] server-props))

(defn store-server-state!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) server-state
  ;
  ; @return (map)
  [db [_ server-state]]
  (assoc-in db [:core :server-handler/server-state] server-state))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-server-props! store-server-props!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-server-state! store-server-state!)
