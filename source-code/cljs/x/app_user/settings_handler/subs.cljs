
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-user.settings-handler.subs
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-user-settings
  ; @usage
  ;  (r user/get-user-settings db)
  ;
  ; @return (map)
  [db _]
  (get-in db [:user :settings-handler/user-settings]))

(defn get-user-settings-item
  ; @param (keyword) item-key
  ;
  ; @usage
  ;  (r user/get-user-settings-item db :my-settings-item)
  ;
  ; @return (*)
  [db [_ item-key]]
  (get-in db [:user :settings-handler/user-settings item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:user/get-user-settings]
(r/reg-sub :user/get-user-settings get-user-settings)

; @usage
;  [:user/get-user-settings-item]
(r/reg-sub :user/get-user-settings-item get-user-settings-item)
