
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler.events
    (:require [x.mid-core.event-handler :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-configs!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) configs
  ;  {:app-config (map)
  ;   :server-config (map)
  ;   :website-config (map)}
  ;
  ; @return (map)
  [db [_ {:keys [app-config server-config website-config]}]]
  (-> db (assoc-in [:core :config-handler/app-config]     app-config)
         (assoc-in [:core :config-handler/server-config]  server-config)
         (assoc-in [:core :config-handler/website-config] website-config)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-configs! store-configs!)
