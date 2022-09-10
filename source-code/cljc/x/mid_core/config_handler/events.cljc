
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

(defn store-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) app-config
  ;
  ; @return (map)
  [db [_ app-config]]
  (assoc-in db [:core :config-handler/app-config] app-config))

(defn store-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-config
  ;
  ; @return (map)
  [db [_ server-config]]
  (assoc-in db [:core :config-handler/server-config] server-config))

(defn store-website-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) website-config
  ;
  ; @return (map)
  [db [_ website-config]]
  (assoc-in db [:core :config-handler/website-config] website-config))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-app-config! store-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-server-config! store-server-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-website-config! store-website-config!)
