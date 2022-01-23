
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.18
; Description:
; Version: v0.1.0.2
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler
    (:require [x.mid-core.event-handler :as event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-config
  ; @usage
  ;  (r a/get-app-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/app-config :data-items]))

(event-handler/reg-sub :core/get-app-config get-app-config)

(defn get-app-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-app-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/app-config :data-items config-item-key]))

(event-handler/reg-sub :core/get-app-config-item get-app-config-item)

(defn get-server-config
  ; @usage
  ;  (r a/get-server-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/server-config :data-items]))

(event-handler/reg-sub :core/get-server-config get-server-config)

(defn get-server-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-server-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/server-config :data-items config-item-key]))

(event-handler/reg-sub :core/get-server-config-item get-server-config-item)

(defn get-site-config
  ; @usage
  ;  (r a/get-site-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/site-config :data-items]))

(event-handler/reg-sub :core/get-site-config get-site-config)

(defn get-site-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-site-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/site-config :data-items config-item-key]))

(event-handler/reg-sub :core/get-site-config-item get-site-config-item)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-configs!
  ; @param (map) configs
  ;  {:app-config (map)
  ;   :server-config (map)
  ;   :site-config (map)}
  ;
  ; @return (map)
  [db [_ {:keys [app-config server-config site-config]}]]
  (-> db (assoc-in [:core/app-config    :data-items] app-config)
         (assoc-in [:core/server-config :data-items] server-config)
         (assoc-in [:core/site-config   :data-items] site-config)))

(event-handler/reg-event-db :core/store-configs! store-configs!)
