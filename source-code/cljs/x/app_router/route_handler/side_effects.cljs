
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.side-effects
    (:require [accountant.core                   :as accountant.core]
              [x.app-core.api                    :as a]
              [x.app-router.route-handler.config :as route-handler.config]))



;; -- Side-effect events ------------------------------------------------------
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
(a/reg-fx :router/configure-navigation! configure-navigation!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :router/navigate! navigate!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :router/navigate-back! navigate-back!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :router/dispatch-current-route! dispatch-current-route!)
