
(ns pretty-inputs.text-field.side-effects
    (:require [activity-listener.api           :as activity-listener]
              [dom.api                         :as dom]
              [fruits.hiccup.api               :as hiccup]
              [keypress-handler.api            :as keypress-handler]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.text-field.config :as text-field.config]
              [pretty-inputs.text-field.env    :as text-field.env]
              [pretty-inputs.text-field.utils  :as text-field.utils]
              [time.api                        :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-field-surface!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; - Stores which field displays its surface currently.
  ; - Only one field can display its surface at a time.
  ; - Ensures that the field is focused, otherwise the surface will not shown.
  ;   E.g., When the 'type-ended' function (what is applied with a delay) calls
  ;         this function ('show-field-surface!'), the user had enough time to leave the field.
  (when (pretty-inputs.engine/input-focused? field-id field-props)
        (pretty-inputs.engine/update-all-input-state! dissoc :surface-visible?)
        (pretty-inputs.engine/update-input-state! field-id assoc :surface-visible? true)))

(defn hide-field-surface!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (pretty-inputs.engine/update-input-state! field-id dissoc :surface-visible?))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [on-enter-props {:key-code 13 :on-keydown-f #(pretty-inputs.engine/input-ENTER-pressed field-id field-props) :in-type-mode? true}
        on-esc-props   {:key-code 27 :on-keydown-f #(pretty-inputs.engine/input-ESC-pressed   field-id field-props) :in-type-mode? true}]
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ENTER on-enter-props)
       (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ESC     on-esc-props)))

(defn dereg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [_ _]
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ENTER)
  (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ESC))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [focus-id (hiccup/value field-id)]
       (if-let [target-element (dom/get-element-by-attribute "data-focus-id" focus-id)]
               (dom/select-content! target-element))) ; <- Experimental solution.
  (keypress-handler/enable-type-mode!)
  (pretty-inputs.engine/input-focused field-id field-props)
  (show-field-surface!                field-id field-props)
  (reg-keypress-events!               field-id field-props))

(defn field-left
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (keypress-handler/disable-type-mode!)
  (pretty-inputs.engine/input-left field-id field-props)
  (hide-field-surface!             field-id field-props)
  (dereg-keypress-events!          field-id field-props))

(defn type-ended
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {}
  [field-id {:keys [on-type-ended-f] :as field-props}]
  ; - This function ('type-ended') is called by the 'value-changed' function
  ;   with a delay after the field's content has been changed.
  ; - If the field's content hasn't changed during that delay, ...
  ;   ... the typing is considered as ended.
  ;   ... the 'on-type-ended-f' function is getting applied.
  ; - This function doesn't take the field's content as its argument, because
  ;   the field's content might changed during the delay.
  (when (activity-listener/idle-time-elapsed? field-id text-field.config/TYPE-ENDED-AFTER)
        (show-field-surface! field-id field-props)
        (let [field-content (text-field.env/get-field-content field-id field-props)]
             (if on-type-ended-f (on-type-ended-f field-content)))))

(defn value-changed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (DOM-event) event
  [field-id field-props event]
  (let [field-content (text-field.utils/on-change-event->field-content field-id field-props event)]
       (pretty-inputs.engine/input-value-changed field-id field-props field-content)
       (activity-listener/reg-last-activity!  field-id)
       (letfn [(f0 [] (type-ended field-id field-props))]
              (time/set-timeout! f0 text-field.config/TYPE-ENDED-AFTER))))
