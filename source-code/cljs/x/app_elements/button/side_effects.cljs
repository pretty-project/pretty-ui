
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
              [re-frame.api          :as r]
              [x.app-environment.api :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id x.environment/focus-element!)))

(defn blur-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id x.environment/blur-element!)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.button/focus-button! focus-button!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.button/blur-button! blur-button!)
