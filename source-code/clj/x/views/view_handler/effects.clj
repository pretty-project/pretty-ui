
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.views.view-handler.effects
    (:require [re-frame.api                :as r :refer [r]]
              [x.views.view-handler.events :as view-handler.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.views/set-error-screen!
  ; @param (metamorphic-event) error-screen
  ;
  ; @usage
  ;  [:x.views/set-error-screen! [:my-error-screen]]
  (fn [{:keys [db]} [_ error-screen]]
      {:db (r view-handler.events/set-error-screen! db error-screen)}))

(r/reg-event-fx :x.views/set-login-screen!
  ; @param (metamorphic-event) login-screen
  ;
  ; @usage
  ;  [:x.views/set-login-screen! [:my-login-screen]]
  (fn [{:keys [db]} [_ login-screen]]
      {:db (r view-handler.events/set-login-screen! db login-screen)}))

(r/reg-event-fx :x.views/set-menu-screen!
  ; @param (metamorphic-event) menu-screen
  ;
  ; @usage
  ;  [:x.views/set-menu-screen! [:my-menu-screen]]
  (fn [{:keys [db]} [_ menu-screen]]
      {:db (r view-handler.events/set-menu-screen! db menu-screen)}))
