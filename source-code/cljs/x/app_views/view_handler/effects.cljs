
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.view-handler.effects
    (:require [re-frame.api                  :as r :refer [r]]
              [x.app-views.view-handler.subs :as view-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views/render-error-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  (fn [{:keys [db]} [_ error-id]]
      (let [error-screen (r view-handler.subs/get-error-screen db)]
           (r/metamorphic-event<-params error-screen error-id))))

(r/reg-event-fx :views/render-login-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [login-screen (r view-handler.subs/get-login-screen db)]
           {:dispatch login-screen})))

(r/reg-event-fx :views/render-menu-screen!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (let [menu-screen (r view-handler.subs/get-menu-screen db)]
           {:dispatch menu-screen})))
