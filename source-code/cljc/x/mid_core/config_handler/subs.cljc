
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler.subs
    (:require [x.mid-core.event-handler :as event-handler :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-config
  ; @usage
  ;  (r a/get-app-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/app-config]))

(defn get-app-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-app-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/app-config item-key]))

(defn get-server-config
  ; @usage
  ;  (r a/get-server-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/server-config]))

(defn get-server-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-server-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/server-config item-key]))

(defn get-site-config
  ; @usage
  ;  (r a/get-site-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/site-config]))

(defn get-site-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-site-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/site-config item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-app-config]
(event-handler/reg-sub :core/get-app-config get-app-config)

; @usage
;  [:core/get-app-config-item :my-item]
(event-handler/reg-sub :core/get-app-config-item get-app-config-item)

; @usage
;  [:core/get-server-config]
(event-handler/reg-sub :core/get-server-config get-server-config)

; @usage
;  [:core/get-server-config-item :my-item]
(event-handler/reg-sub :core/get-server-config-item get-server-config-item)

; @usage
;  [:core/get-site-config]
(event-handler/reg-sub :core/get-site-config get-site-config)

; @usage
;  [:core/get-app-site-item :my-item]
(event-handler/reg-sub :core/get-site-config-item get-site-config-item)
