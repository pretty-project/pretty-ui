
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.view-handler.effects
    (:require [x.app-core.api                :as a :refer [r]]
              [x.app-views.view-handler.subs :as view-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-error-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  (fn [{:keys [db]} [_ error-id]]
      (let [error-screen (r view-handler.subs/get-error-screen db)]
           (a/metamorphic-event<-params error-screen error-id))))

(a/reg-event-fx
  :views/render-login-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [login-screen (r view-handler.subs/get-login-screen db)]
           {:dispatch login-screen})))

(a/reg-event-fx
  :views/render-menu-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [menu-screen (r view-handler.subs/get-menu-screen db)]
           {:dispatch menu-screen})))
