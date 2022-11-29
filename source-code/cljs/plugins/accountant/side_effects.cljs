
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.accountant.side-effects
    (:require [accountant.core]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn configure-navigation!
  ; @param (map) navigation-props
  ;  {:nav-handler (function)
  ;   :path-exists? (function)
  ;   :reload-same-path? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  (configure-navigation! {...})
  [navigation-props]
  ; Create and configure HTML5 history navigation.
  (accountant.core/configure-navigation! navigation-props))

(defn unconfigure-navigation!
  ; @usage
  ;  (unconfigure-navigation! {...})
  [_]
  ; Teardown HTML5 history navigation.
  (accountant.core/unconfigure-navigation!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn navigate!
  ; @param (string) route-string
  ;
  ; @usage
  ;  (navigate! "...")
  [route-string]
  ; Add a browser history entry. Updates window/location.
  (accountant.core/navigate! route-string))

(defn dispatch-current!
  ; @usage
  ;  (dispatch-current! "...")
  [_]
  ; Dispatch current URI path. Call the nav-handler function.
  (accountant.core/dispatch-current!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:accountant/configure-navigation! {...}]
(r/reg-fx :accountant/configure-navigation! configure-navigation!)

; @usage
;  [:accountant/unconfigure-navigation!]
(r/reg-fx :accountant/unconfigure-navigation! unconfigure-navigation!)

; @usage
;  [:accountant/navigate! "..."]
(r/reg-fx :accountant/navigate! navigate!)

; @usage
;  [:accountant/dispatch-current!]
(r/reg-fx :accountant/dispatch-current! dispatch-current!)
