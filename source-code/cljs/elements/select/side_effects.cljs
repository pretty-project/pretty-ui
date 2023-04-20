
(ns elements.select.side-effects
    (:require [keypress-handler.api :as keypress-handler]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (let [on-escape-props {:key-code 27 :required? true :on-keyup #(r/dispatch [:elements.select/ESC-pressed   select-id select-props])}
        on-enter-props  {:key-code 13 :required? true :on-keyup #(r/dispatch [:elements.select/ENTER-pressed select-id select-props])}]
       (keypress-handler/reg-keypress-event! :elements.select/ESC   on-escape-props)
       (keypress-handler/reg-keypress-event! :elements.select/ENTER on-enter-props)))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  (keypress-handler/remove-keypress-event! :elements.select/ESC)
  (keypress-handler/remove-keypress-event! :elements.select/ENTER))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :elements.select/reg-keypress-events! reg-keypress-events!)

; @ignore
(r/reg-fx :elements.select/remove-keypress-events! remove-keypress-events!)
