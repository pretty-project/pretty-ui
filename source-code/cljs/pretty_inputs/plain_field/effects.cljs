
(ns pretty-inputs.plain-field.effects
    (:require [pretty-inputs.input.env          :as input.env]
              [pretty-inputs.plain-field.env    :as plain-field.env]
              [pretty-inputs.plain-field.events :as plain-field.events]
              [re-frame.api                       :as r :refer [r]]
              [pretty-build-kit.api]))
              
;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.plain-field/field-did-mount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofocus? (boolean)(opt)
  ;  :initial-value (*)(opt)
  ;  :on-mount (function or Re-Frame metamorphic-event)(opt)
  ;  :value-path (Re-Frame path vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? initial-value on-mount value-path] :as field-props}]]
      ; The autofocus has to be delayed; otherwise, the caret would shown up not at the end of the content.
      (let [stored-value (get-in db value-path)]
           {:dispatch-later [(if autofocus?    {:ms 50 :fx [:pretty-inputs.plain-field/focus-field! field-id]})]
            :dispatch-n     [(if initial-value [:pretty-inputs.plain-field/use-initial-value! field-id field-props])
                             [:pretty-build-kit/dispatch-event-handler! on-mount (or initial-value stored-value)]]})))

(r/reg-event-fx :pretty-inputs.plain-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :on-unmount (function or Re-Frame metamorphic-event)(opt)
  ;  :value-path (Re-Frame path vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autoclear? on-unmount value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           {:db       (if autoclear? (r plain-field.events/clear-value! db field-id field-props) db)
            :dispatch [:pretty-build-kit/dispatch-event-handler! on-unmount stored-value]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.plain-field/use-initial-value!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r plain-field.events/use-initial-value! db field-id field-props)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.plain-field/type-ended
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-type-ended (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props}]]
      ; BUG#6071 (source-code/cljs/pretty_elements/plain_field/side_effects.cljs)
      ;
      ; If the field is not being focused anymore when this effect is dispatched
      ; (it's being dispatched with a delay) ...
      ; ... no need to display the field surface anymore.
      ; ... no need to write the actual field value into the Re-Frame state,
      ;     because the ':pretty-inputs.plain-field/field-blurred' effect already did it
      ;     (and the field could be unmounted already when this effect happens).
      (let [field-content (plain-field.env/get-field-content field-id)]
           (if-let [field-focused? (input.env/input-focused? field-id)]
                   {:db       (r plain-field.events/store-value! db field-id field-props field-content)
                    :fx       [:pretty-inputs.plain-field/show-surface! field-id]
                    :dispatch [:pretty-build-kit/dispatch-event-handler! on-type-ended field-content]}
                   {:dispatch [:pretty-build-kit/dispatch-event-handler! on-type-ended field-content]}))))

(r/reg-event-fx :pretty-inputs.plain-field/field-blurred
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-blur (function or Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-blur] :as field-props}]]
      ; - When the user leaves a field it writes its actual field content into the Re-Frame state immediately.
      ; - Normally this state-writing action happens delayed after the last key is pressed
      ;   (when the ':pretty-inputs.plain-field/type-ended' effect is dispatched),
      ;   but there could be a case when it has to happen immediately.
      ;   E.g., The user ends typing and quickly clicks on a button that triggers the validation
      ;         of the field or a validation of a form that contains the field.
      ;         When the validator functions are being applied on the stored value of
      ;         the field (stored in the Re-Frame state) it's important to the state
      ;         contains the actual field content!
      ;         The solution is that the 'on-mouse-down' event of the button fires the
      ;         'on-blur' event of the field and that event writes the field content into
      ;         the state immediately and when the 'on-mouse-up' event of the button
      ;         triggers the validation, the actual field content will be already in the application state.
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:db         (r plain-field.events/store-value! db field-id field-props field-content)
            :dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-blur field-content]]
            :fx-n       [[:pretty-inputs.plain-field/hide-surface!      field-id]
                         [:pretty-inputs.input/unmark-input-as-focused! field-id]
                         [:pretty-inputs.plain-field/quit-type-mode!    field-id]]})))

(r/reg-event-fx :pretty-inputs.plain-field/field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-focus (function or Re-Frame metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [on-focus]}]]
      (let [field-content (plain-field.env/get-field-content field-id)]
           {:dispatch-n [[:pretty-build-kit/dispatch-event-handler! on-focus field-content]]
            :fx-n       [[:pretty-inputs.plain-field/show-surface!    field-id]
                         [:pretty-inputs.input/mark-input-as-focused! field-id]
                         [:pretty-inputs.plain-field/set-type-mode!   field-id]]})))
