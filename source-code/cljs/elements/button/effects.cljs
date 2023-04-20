
(ns elements.button.effects
    (:require [re-frame.api :as r]
              [reagent.api  :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.button/button-did-mount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  (fn [_ [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:fx [:elements.button/reg-keypress-event! button-id button-props]})))

(r/reg-event-fx :elements.button/button-did-update
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (?) %
  (fn [_ [_ button-id %]]
      (let [[_ {:keys [keypress on-click] :as button-props}] (reagent/arguments %)]
           (when keypress {:fx [:elements.button/remove-keypress-event! button-id button-props]}
                          {:fx [:elements.button/reg-keypress-event!    button-id button-props]}))))

(r/reg-event-fx :elements.button/button-will-unmount
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  (fn [_ [_ button-id {:keys [keypress] :as button-props}]]
      (if keypress {:fx [:elements.button/remove-keypress-event! button-id button-props]})))
