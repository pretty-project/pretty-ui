
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button.side-effects
    (:require [mid-fruits.hiccup     :as hiccup]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id environment/focus-element!)))

(defn blur-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id environment/blur-element!)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.button/focus-button! focus-button!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.button/blur-button! blur-button!)
