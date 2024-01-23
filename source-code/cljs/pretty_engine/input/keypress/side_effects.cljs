
(ns pretty-engine.input.keypress.side-effects
    (:require [pretty-engine.input.value.env :as input.value.env]
              [pretty-engine.input.utils :as input.utils]
              [keypress-handler.api :as keypress-handler]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @usage
  ; (input-ENTER-pressed :my-input {:on-enter-f #(...)})
  [input-id {:keys [on-enter-f] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-enter-f (on-enter-f input-displayed-value))))

(defn input-ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @usage
  ; (input-ESC-pressed :my-input {:on-escape-f #(...)})
  [input-id {:keys [on-escape-f] :as input-props}]
  (let [input-displayed-value (input.value.env/get-input-displayed-value input-id input-props)]
       (if on-escape-f (on-escape-f input-displayed-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-input-keypress-events!
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @usage
  ; (reg-input-keypress-events! :my-input {:on-enter-f  #(...)
  ;                                        :on-escape-f #(...)})
  [input-id {:keys [on-enter-f on-escape-f] :as input-props}]
  ; @note (pretty-engine.layout.keypress.side-effects#4401)
  (let [on-enter-id        (input.utils/input-id->subitem-id input-id :input-ENTER-pressed)
        on-escape-id       (input.utils/input-id->subitem-id input-id :input-ESC-pressed)
        on-enter-pressed-f (fn [_] (input-ENTER-pressed input-id input-props))
        on-esc-pressed-f   (fn [_] (input-ESC-pressed   input-id input-props))
        on-enter-props     {:key-code 13 :in-type-mode? true :exclusive? true :on-keydown-f on-enter-pressed-f}
        on-escape-props    {:key-code 27 :in-type-mode? true :exclusive? true :on-keydown-f on-esc-pressed-f}]
       (if on-enter-f  (keypress-handler/reg-keypress-event! on-enter-id  on-enter-props))
       (if on-escape-f (keypress-handler/reg-keypress-event! on-escape-id on-escape-props))))

(defn dereg-input-keypress-events!
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {}
  ;
  ; @usage
  ; (dereg-input-keypress-events! :my-input {:on-enter-f  #(...)
  ;                                          :on-escape-f #(...)})
  [input-id {:keys [on-enter-f on-escape-f]}]
  ; @note (pretty-engine.layout.keypress.side-effects#4401)
  (let [on-enter-id  (input.utils/input-id->subitem-id input-id :input-ENTER-pressed)
        on-escape-id (input.utils/input-id->subitem-id input-id :input-ESC-pressed)]
       (if on-enter-f  (keypress-handler/dereg-keypress-event! on-enter-id))
       (if on-escape-f (keypress-handler/dereg-keypress-event! on-escape-id))))
