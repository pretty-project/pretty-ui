
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.config-handler.subs
    (:require [re-frame.api :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-config
  ; @usage
  ;  (r get-app-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/app-config]))

(defn get-app-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r get-app-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/app-config item-key]))

(defn get-server-config
  ; @usage
  ;  (r get-server-config db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:core :config-handler/server-config]))

(defn get-server-config-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r get-server-config-item db :my-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:core :config-handler/server-config item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:core/get-app-config]
(r/reg-sub :core/get-app-config get-app-config)

; @usage
;  [:core/get-app-config-item :my-item]
(r/reg-sub :core/get-app-config-item get-app-config-item)

; @usage
;  [:core/get-server-config]
(r/reg-sub :core/get-server-config get-server-config)

; @usage
;  [:core/get-server-config-item :my-item]
(r/reg-sub :core/get-server-config-item get-server-config-item)
