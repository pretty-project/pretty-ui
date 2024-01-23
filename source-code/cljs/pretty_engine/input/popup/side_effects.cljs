
(ns pretty-engine.input.popup.side-effects
    (:require [keypress-handler.api                   :as keypress-handler]
              [pretty-engine.input.focus.side-effects :as input.focus.side-effects]
              [pretty-engine.input.state.side-effects :as input.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id _]
  (input.state.side-effects/update-input-state! input-id dissoc :popup-rendered?)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.input-popup/ESC))

(defn render-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (input.focus.side-effects/mark-input-as-focused! input-id input-props)
  (input.state.side-effects/update-all-input-state! dissoc :popup-rendered?)
  (input.state.side-effects/update-input-state! input-id assoc :popup-rendered? true)
  (let [close-input-popup-f (fn [] (close-input-popup! input-id input-props))
        on-escape-props     {:key-code 27 :in-type-mode? true :exclusive? true :on-keyup-f close-input-popup-f}]
       (keypress-handler/reg-keypress-event! :pretty-inputs.input-popup/ESC on-escape-props)))

(defn update-input-popup!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  [input-id input-props]
  (render-input-popup! input-id input-props))
