
(ns elements.button.side-effects
    (:require [dom.api      :as dom]
              [hiccup.api   :as hiccup]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-button!
  ; @ignore
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (if-let [button-body-element (dom/get-element-by-id button-body-id)]
               (dom/focus-element! button-body-element))))

(defn blur-button!
  ; @ignore
  ;
  ; @param (keyword) button-id
  [button-id]
  (let [button-body-id (hiccup/value button-id "body")]
       (if-let [button-body-element (dom/get-element-by-id button-body-id)]
               (dom/blur-element! button-body-element))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :elements.button/focus-button! focus-button!)

; @ignore
(r/reg-fx :elements.button/blur-button! blur-button!)
