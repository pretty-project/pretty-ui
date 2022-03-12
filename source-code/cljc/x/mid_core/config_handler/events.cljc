
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler.events
    (:require [x.mid-core.event-handler :as event-handler :refer [r]]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-configs!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) configs
  ;  {:app-config (map)
  ;   :server-config (map)
  ;   :site-config (map)}
  ;
  ; @return (map)
  [db [_ {:keys [app-config server-config site-config]}]]
  (-> db (assoc-in [:core :config-handler/app-config]    app-config)
         (assoc-in [:core :config-handler/server-config] server-config)
         (assoc-in [:core :config-handler/site-config]   site-config)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-configs! store-configs!)
