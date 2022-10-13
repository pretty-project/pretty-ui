
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.side-effects
    (:require [accountant.core                   :as accountant.core]
              [re-frame.api                      :as r]
              [x.app-router.route-handler.config :as route-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- configure-navigation!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (accountant.core/configure-navigation! route-handler.config/ACCOUNTANT-CONFIG))

(defn- navigate!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) route-string
  [route-string]
  (accountant.core/navigate! route-string))

(defn- navigate-back!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [history (.-history js/window)]
       (.back history)))

(defn- dispatch-current-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (accountant.core/dispatch-current!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :router/configure-navigation! configure-navigation!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :router/navigate! navigate!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :router/navigate-back! navigate-back!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :router/dispatch-current-route! dispatch-current-route!)
