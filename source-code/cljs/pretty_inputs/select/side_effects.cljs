
(ns pretty-inputs.select.side-effects
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
  (let [on-escape-props {:key-code 27 :required? true :on-keyup-f #(r/dispatch [:pretty-inputs.select/ESC-pressed   select-id select-props])}
        on-enter-props  {:key-code 13 :required? true :on-keyup-f #(r/dispatch [:pretty-inputs.select/ENTER-pressed select-id select-props])}]
       (keypress-handler/reg-keypress-event! :pretty-inputs.select/ESC   on-escape-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.select/ENTER on-enter-props)))

(defn dereg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  (keypress-handler/dereg-keypress-event! :pretty-inputs.select/ESC)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.select/ENTER))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) select-id
; @param (map) select-props
(r/reg-fx :pretty-inputs.select/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) select-id
; @param (map) select-props
(r/reg-fx :pretty-inputs.select/dereg-keypress-events! dereg-keypress-events!)
