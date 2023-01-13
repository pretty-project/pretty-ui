
(ns elements.button.side-effects
    (:require [hiccup.api        :as hiccup]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))


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
