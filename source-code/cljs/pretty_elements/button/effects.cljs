
(ns pretty-elements.button.effects
    (:require [re-frame.api :as r]
              [reagent.api  :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.button/button-did-mount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  (fn [_ [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:fx [:pretty-elements.button/reg-keypress-event! button-id button-props]})))

(r/reg-event-fx :pretty-elements.button/button-did-update
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (?) %
  (fn [_ [_ button-id %]]
      (let [[_ {:keys [keypress] :as button-props}] (reagent/arguments %)]
           (when keypress {:fx [:pretty-elements.button/dereg-keypress-event! button-id button-props]}
                          {:fx [:pretty-elements.button/reg-keypress-event!   button-id button-props]}))))

(r/reg-event-fx :pretty-elements.button/button-will-unmount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  (fn [_ [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:fx [:pretty-elements.button/dereg-keypress-event! button-id button-props]})))
