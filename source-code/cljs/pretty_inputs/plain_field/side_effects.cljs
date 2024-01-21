
(ns pretty-inputs.plain-field.side-effects
    (:require [activity-listener.api            :as activity-listener]
              [dom.api                          :as dom]
              [fruits.hiccup.api                :as hiccup]
              [keypress-handler.api             :as keypress-handler]
              [pretty-inputs.core.env           :as core.env]
              [pretty-inputs.core.side-effects  :as core.side-effects]
              [pretty-inputs.plain-field.config :as plain-field.config]
              [pretty-inputs.plain-field.utils  :as plain-field.utils]
              [time.api                         :as time]))

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
  (when (core.env/input-focused? field-id field-props)
        (core.side-effects/update-all-input-state! dissoc :surface-visible?)
        (core.side-effects/update-input-state! field-id assoc :surface-visible? true)))

(defn hide-field-surface!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (core.side-effects/update-input-state! field-id dissoc :surface-visible?))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [on-enter-props {:key-code 13 :on-keydown-f #(core.side-effects/ENTER-pressed field-id field-props) :required? true}
        on-esc-props   {:key-code 27 :on-keydown-f #(core.side-effects/ESC-pressed   field-id field-props) :required? true}]
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
  (keypress-handler/set-type-mode!)
  (core.side-effects/input-focused field-id field-props)
  (show-field-surface!             field-id field-props)
  (reg-keypress-events!            field-id field-props))

(defn field-left
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (keypress-handler/quit-type-mode!)
  (core.side-effects/input-left field-id field-props)
  (hide-field-surface!          field-id field-props)
  (dereg-keypress-events!       field-id field-props))

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
  (when (activity-listener/idle-time-elapsed? field-id plain-field.config/TYPE-ENDED-AFTER)
        (show-field-surface! field-id field-props)
        (let [field-content (core.env/get-input-displayed-value field-id field-props)]
             (if on-type-ended-f (on-type-ended-f field-content)))))

(defn value-changed
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (DOM-event) event
  [field-id field-props event]
  (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)]
       (core.side-effects/input-value-changed field-id field-props field-content)
       (activity-listener/reg-last-activity!  field-id)
       (letfn [(f0 [] (type-ended field-id field-props))]
              (time/set-timeout! f0 plain-field.config/TYPE-ENDED-AFTER))))
