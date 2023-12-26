
(ns pretty-elements.button.side-effects
    (:require [dom.api              :as dom]
              [fruits.hiccup.api    :as hiccup]
              [keypress-handler.api :as keypress-handler]
              [re-frame.api         :as r]))

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

(defn key-pressed
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id _]
  (focus-button! button-id))

(defn key-released
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:on-click (function)(opt)}
  [button-id {:keys [on-click]}]
  (blur-button! button-id)
  (on-click))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-event!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)
  ;   {:key-code (integer)
  ;    :required? (boolean)(opt)}}
  [button-id {:keys [keypress] :as button-props}]
  (keypress-handler/reg-keypress-event! button-id {:exclusive? (:exclusive? keypress)
                                                   :key-code   (:key-code   keypress)
                                                   :required?  (:required?  keypress)
                                                   :on-keydown (fn [_] (key-pressed  button-id button-props))
                                                   :on-keyup   (fn [_] (key-released button-id button-props))
                                                   :prevent-default? true}))

(defn remove-keypress-event!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id _]
  (keypress-handler/remove-keypress-event! button-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) button-id
(r/reg-fx :pretty-elements.button/reg-keypress-event! reg-keypress-event!)

; @ignore
;
; @param (keyword) button-id
(r/reg-fx :pretty-elements.button/remove-keypress-event! remove-keypress-event!)
