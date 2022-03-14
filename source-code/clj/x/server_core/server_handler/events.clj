
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.server-handler.events
    (:require [mid-fruits.map              :refer [dissoc-in]]
              [x.server-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-server-state!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:core :server-handler/meta-items]))

(defn store-server-state!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (?) server-state
  ;
  ; @return (map)
  [db [_ server-state]]
  (assoc-in db [:core :server-handler/meta-items] server-state))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/reset-server-state! reset-server-state!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-server-state! store-server-state!)
