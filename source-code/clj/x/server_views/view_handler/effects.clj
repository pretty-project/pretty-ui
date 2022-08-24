
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.effects
    (:require [x.server-core.api                  :as a :refer [r]]
              [x.server-views.view-handler.events :as view-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/set-error-screen!
  ; @param (metamorphic-event) error-screen
  ;
  ; @usage
  ;  [:user/set-error-screen! [:my-error-screen]]
  (fn [{:keys [db]} [_ error-screen]]
      {:db (r view-handler.events/set-error-screen! db error-screen)}))

(a/reg-event-fx
  :views/set-login-screen!
  ; @param (metamorphic-event) login-screen
  ;
  ; @usage
  ;  [:user/set-login-screen! [:my-login-screen]]
  (fn [{:keys [db]} [_ login-screen]]
      {:db (r view-handler.events/set-login-screen! db login-screen)}))

(a/reg-event-fx
  :views/set-menu-screen!
  ; @param (metamorphic-event) menu-screen
  ;
  ; @usage
  ;  [:user/set-menu-screen! [:my-menu-screen]]
  (fn [{:keys [db]} [_ menu-screen]]
      {:db (r view-handler.events/set-menu-screen! db menu-screen)}))
