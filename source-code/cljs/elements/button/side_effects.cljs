
(ns elements.button.side-effects
    (:require [dom.api           :as dom]
              [hiccup.api        :as hiccup]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-button!
  ; @ignore
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id x.environment/focus-element!)))

(defn blur-button!
  ; @ignore
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (-> button-body-id x.environment/blur-element!)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :elements.button/focus-button! focus-button!)

; @ignore
(r/reg-fx :elements.button/blur-button! blur-button!)
