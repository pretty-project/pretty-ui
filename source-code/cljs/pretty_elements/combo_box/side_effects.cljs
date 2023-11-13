
(ns pretty-elements.combo-box.side-effects
    (:require [pretty-elements.combo-box.env            :as combo-box.env]
              [pretty-elements.combo-box.state          :as combo-box.state]
              [pretty-elements.plain-field.side-effects :as plain-field.side-effects]
              [keypress-handler.api              :as keypress-handler]
              [re-frame.api                      :as r]
              [seqable.api                        :as seqable]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn highlight-next-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)]
       ; If no option selected, then the first option has to be selected at the first time ...
       (if (nil? highlighted-option-dex)
           (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id 0)
           (let [next-option-dex (seqable/next-dex rendered-options highlighted-option-dex)]
                (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id next-option-dex)))))

(defn highlight-prev-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)
        prev-option-dex        (seqable/prev-dex rendered-options highlighted-option-dex)]
       (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id prev-option-dex)))

(defn discard-option-highlighter!
  ; @ignore
  ;
  ; @param (keyword) box-id
  [box-id]
  (swap! combo-box.state/OPTION-HIGHLIGHTS dissoc box-id))

(defn use-selected-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-label-f (function)}
  ; @param (*) selected-option
  [box-id {:keys [option-label-f] :as box-props} selected-option]
  (let [option-label (option-label-f selected-option)]
       (plain-field.side-effects/set-field-content! box-id option-label)))

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
  ; by using the ':pretty-elements.text-field/ESC' and ':pretty-elements.text-field/ENTER' keypress event IDs.
  ; The overwritten keypress events' functionality is implemented in the 'combo-box'
  ; field keypress events.
  ;
  ; The UP and DOWN keypress events has similar names (for sake of consistency).
  (let [on-down-props  {:key-code 40 :on-keydown #(r/dispatch [:pretty-elements.combo-box/DOWN-pressed  box-id box-props]) :required? true :prevent-default? true}
        on-up-props    {:key-code 38 :on-keydown #(r/dispatch [:pretty-elements.combo-box/UP-pressed    box-id box-props]) :required? true :prevent-default? true}
        on-esc-props   {:key-code 27 :on-keydown #(r/dispatch [:pretty-elements.combo-box/ESC-pressed   box-id box-props]) :required? true}
        on-enter-props {:key-code 13 :on-keydown #(r/dispatch [:pretty-elements.combo-box/ENTER-pressed box-id box-props]) :required? true}]
       (keypress-handler/reg-keypress-event! :pretty-elements.text-field/DOWN   on-down-props)
       (keypress-handler/reg-keypress-event! :pretty-elements.text-field/UP       on-up-props)
       (keypress-handler/reg-keypress-event! :pretty-elements.text-field/ESC     on-esc-props)
       (keypress-handler/reg-keypress-event! :pretty-elements.text-field/ENTER on-enter-props)))

(defn remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [_ _]
  ; XXX#4156
  (keypress-handler/remove-keypress-event! :pretty-elements.text-field/DOWN)
  (keypress-handler/remove-keypress-event! :pretty-elements.text-field/UP)
  (keypress-handler/remove-keypress-event! :pretty-elements.text-field/ESC)
  (keypress-handler/remove-keypress-event! :pretty-elements.text-field/ENTER))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/highlight-next-option! highlight-next-option!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/highlight-prev-option! highlight-prev-option!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/discard-option-highlighter! discard-option-highlighter!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/use-selected-option! use-selected-option!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/reg-keypress-events! reg-keypress-events!)

; @ignore
;
; @param (keyword) box-id
; @param (map) box-props
(r/reg-fx :pretty-elements.combo-box/remove-keypress-events! remove-keypress-events!)
