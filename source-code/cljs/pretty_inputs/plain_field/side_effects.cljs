
(ns pretty-inputs.plain-field.side-effects
    (:require [activity-listener.api            :as activity-listener]
              [dom.api                          :as dom]
              [fruits.hiccup.api                :as hiccup]
              [keypress-handler.api             :as keypress-handler]
              [keypress-handler.api             :as keypress-handler]
              [pretty-inputs.core.env           :as core.env]
              [pretty-inputs.core.side-effects  :as core.side-effects]
              [pretty-inputs.plain-field.config :as plain-field.config]
              [pretty-inputs.plain-field.state  :as plain-field.state]
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
  ;   E.g., When the 'type-ended' function (that is applied with a delay) calls
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
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (Re-Frame metamorphic-event)(opt)}
  [field-id {:keys [emptiable? on-enter] :as field-props}]
  (let []));on-esc-props   {:key-code 27 :on-keydown #(r/dispatch [:pretty-inputs.text-field/ESC-pressed   field-id field-props]) :required? true}
        ;on-enter-props {:key-code 13 :on-keydown #(r/dispatch [:pretty-inputs.text-field/ENTER-pressed field-id field-props]) :required? true}]
       ;(if emptiable? (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ESC     on-esc-props))
       ;(if on-enter   (keypress-handler/reg-keypress-event! :pretty-inputs.text-field/ENTER on-enter-props))))

(defn dereg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:emptiable? (boolean)(opt)
  ;  :on-enter (Re-Frame metamorphic-event)(opt)}
  [_ {:keys [emptiable? on-enter]}]
  (if emptiable? (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ESC))
  (if on-enter   (keypress-handler/dereg-keypress-event! :pretty-inputs.text-field/ENTER)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
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
  ; {}
  ; @param (DOM-event) event
  [field-id {:keys [on-changed-f] :as field-props} event]
  (let [field-content (plain-field.utils/on-change-event->field-content field-id field-props event)]
       (activity-listener/reg-last-activity!        field-id)
       (core.side-effects/set-input-internal-value! field-id field-props field-content)
       (core.side-effects/set-input-external-value! field-id field-props field-content)
       (letfn [(f0 [] (type-ended field-id field-props))]
              (time/set-timeout! f0 plain-field.config/TYPE-ENDED-AFTER))
       (if on-changed-f (on-changed-f field-content))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-field-content!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  ;
  ; BUG#3401
  ; The 'field-content' has to be converted to string type!
  ; It may occur, that a non-seqable type (e.g., integer) being written into
  ; the field and the empty? function throws an error in case of taking
  ; a non-seqable value as its argument.
  (swap! plain-field.state/FIELD-CONTENTS assoc field-id (str field-content)))

(defn set-field-output!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (string) field-content
  [field-id field-content]
  ; HACK#9910
  (swap! plain-field.state/FIELD-OUTPUTS assoc field-id field-content))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (set-field-content! field-id ""))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-field!
  ; @ignore
  ;
  ; @param (keyword) field-id
  [field-id]
  (let [field-input-id (hiccup/value field-id "input")]
       (when-let [field-input-element (dom/get-element-by-id field-input-id)]
                 (dom/move-caret-to-end! field-input-element)
                 (dom/focus-element!     field-input-element))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
