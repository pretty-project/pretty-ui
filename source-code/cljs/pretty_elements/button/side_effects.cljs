
(ns pretty-elements.button.side-effects
    (:require [keypress-handler.api              :as keypress-handler]
              [pretty-elements.core.side-effects :as core.side-effects]
              [re-frame.api                      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn key-pressed
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id _]
  (core.side-effects/focus-element! button-id))

(defn key-released
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:on-click-f (function)(opt)}
  [button-id {:keys [on-click-f]}]
  (core.side-effects/blur-element! button-id)
  (on-click-f))

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
  (keypress-handler/reg-keypress-event! button-id {:exclusive?   (:exclusive? keypress)
                                                   :key-code     (:key-code   keypress)
                                                   :required?    (:required?  keypress)
                                                   :on-keydown-f (fn [_] (key-pressed  button-id button-props))
                                                   :on-keyup-f   (fn [_] (key-released button-id button-props))
                                                   :prevent-default? true}))

(defn dereg-keypress-event!
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id _]
  (keypress-handler/dereg-keypress-event! button-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) button-id
(r/reg-fx :pretty-elements.button/reg-keypress-event! reg-keypress-event!)

; @ignore
;
; @param (keyword) button-id
(r/reg-fx :pretty-elements.button/dereg-keypress-event! dereg-keypress-event!)
