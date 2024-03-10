
(ns pretty-inputs.combo-box.side-effects
    (:require [fruits.seqable.api            :as seqable]
              [keypress-handler.api          :as keypress-handler]
              [pretty-inputs.combo-box.env   :as combo-box.env]
              [re-frame.api                  :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------






















(defn highlight-next-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)]))
       ; If no option selected, then the first option must be selected at the first time ...
       ;(if (nil? highlighted-option-dex))))
           ;(swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id 0)
           ;(let [next-option-dex (seqable/next-dex rendered-options highlighted-option-dex)]
            ;    (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id next-option-dex)))))

(defn highlight-prev-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)
        prev-option-dex        (seqable/prev-dex rendered-options highlighted-option-dex)]))
       ;(swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id prev-option-dex)))

(defn discard-option-highlighter!
  ; @ignore
  ;
  ; @param (keyword) box-id
  [box-id])
  ;(swap! combo-box.state/OPTION-HIGHLIGHTS dissoc box-id))

(defn use-selected-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-label-f (function)}
  ; @param (*) selected-option
  [box-id {:keys [option-label-f] :as box-props} selected-option]
  (let [option-label (option-label-f selected-option)]))
       ;(text-field.side-effects/set-field-content! box-id option-label)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  ; XXX#4156
  ; Overwrites the default ESC and ENTER keypress events of the 'text-field' element
  ; by using the ':pretty-inputs.text-field/ESC' and ':pretty-inputs.text-field/ENTER' keypress event IDs.
  ; The overwritten keypress events' functionality is implemented in the 'combo-box'
  ; field keypress events.
  ;
  ; The UP and DOWN keypress events has similar names (for sake of consistency).
  (let [on-down-props  {:key-code 40 :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/DOWN-pressed  box-id box-props]) :in-type-mode? true :prevent-default? true}
        on-up-props    {:key-code 38 :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/UP-pressed    box-id box-props]) :in-type-mode? true :prevent-default? true}
        on-esc-props   {:key-code 27 :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/ESC-pressed   box-id box-props]) :in-type-mode? true}
        on-enter-props {:key-code 13 :on-keydown-f #(r/dispatch [:pretty-inputs.combo-box/ENTER-pressed box-id box-props]) :in-type-mode? true}]
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/DOWN   on-down-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/UP       on-up-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ESC     on-esc-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ENTER on-enter-props)))

(defn dereg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [_ _]
  ; XXX#4156
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/DOWN)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/UP)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ESC)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ENTER))
