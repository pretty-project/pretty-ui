
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.config-handler.events
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-app-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) app-config
  ;
  ; @return (map)
  [db [_ app-config]]
  (assoc-in db [:x.core :config-handler/app-config] app-config))

(defn store-server-config!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) server-config
  ;
  ; @return (map)
  [db [_ server-config]]
  (assoc-in db [:x.core :config-handler/server-config] server-config))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.core/store-app-config! store-app-config!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.core/store-server-config! store-server-config!)
