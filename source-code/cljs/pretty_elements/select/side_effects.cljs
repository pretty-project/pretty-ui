
(ns pretty-elements.select.side-effects
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
  (let [on-escape-props {:key-code 27 :required? true :on-keyup #(r/dispatch [:pretty-elements.select/ESC-pressed   select-id select-props])}
        on-enter-props  {:key-code 13 :required? true :on-keyup #(r/dispatch [:pretty-elements.select/ENTER-pressed select-id select-props])}]
       (keypress-handler/reg-keypress-event! :pretty-elements.select/ESC   on-escape-props)
       (keypress-handler/reg-keypress-event! :pretty-elements.select/ENTER on-enter-props)))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [_ _]
  (keypress-handler/remove-keypress-event! :pretty-elements.select/ESC)
  (keypress-handler/remove-keypress-event! :pretty-elements.select/ENTER))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) select-id
; @param (map) select-props
(r/reg-f :pretty-elements.select/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) select-id
; @param (map) select-props
(r/reg-f :pretty-elements.select/remove-keypress-events! remove-keypress-events!)
