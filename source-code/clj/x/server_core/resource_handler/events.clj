
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler.events
    (:require [x.server-core.event-handler :as event-handler]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-resource-handler-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) resource-handler-options
  ;
  ; @return (map)
  [db [_ resource-handler-options]]
  (assoc-in db [:core :resource-handler/options] resource-handler-options))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-resource-handler-options! store-resource-handler-options!)
