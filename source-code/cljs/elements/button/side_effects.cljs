
(ns elements.button.side-effects
    (:require [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [hiccup.api                    :as hiccup]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id element.side-effects/focus-element!)))

(defn blur-button!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id element.side-effects/blur-element!)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.button/focus-button! focus-button!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.button/blur-button! blur-button!)
