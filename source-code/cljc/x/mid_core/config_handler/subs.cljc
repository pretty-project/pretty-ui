
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.config-handler.subs
    (:require [x.mid-core.event-handler :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
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

(defn get-website-config
  ; @usage
  ;  (r a/get-website-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/website-config]))

(defn get-website-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r a/get-website-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/website-config item-key]))



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
;  [:core/get-website-config]
(event-handler/reg-sub :core/get-website-config get-website-config)

; @usage
;  [:core/get-website-config-item :my-item]
(event-handler/reg-sub :core/get-website-config-item get-website-config-item)
