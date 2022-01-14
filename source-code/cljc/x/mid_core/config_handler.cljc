
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.18
; Description:
; Version: v0.1.0.2
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler
    (:require [x.mid-core.event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-config
  ; @usage
  ;  (r a/get-app-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/app-config :data-items]))

(defn get-app-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-app-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/app-config :data-items config-item-key]))

(defn get-server-config
  ; @usage
  ;  (r a/get-server-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/server-config :data-items]))

(defn get-server-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-server-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/server-config :data-items config-item-key]))

(defn get-site-config
  ; @usage
  ;  (r a/get-site-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core/site-config :data-items]))

(defn get-site-config-item
  ; @param (keyword) config-item-key
  ;
  ; @usage
  ;  (r a/get-site-config-item db :my-config-item)
  ;
  ; @return (*)
  [db [_ config-item-key]]
  (get-in db [:core/site-config :data-items config-item-key]))



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
