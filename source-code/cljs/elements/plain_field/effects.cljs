
(ns elements.plain-field.effects
    (:require [elements.input.env          :as input.env]
              [elements.plain-field.events :as plain-field.events]
              [elements.plain-field.env    :as plain-field.env]
              [re-frame.api                :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/field-did-mount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofocus? (boolean)(opt)
  ;  :initial-value (*)(opt)
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :value-path (Re-Frame path vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? initial-value on-mount value-path] :as field-props}]]
      ; The autofocus has to be delayed, otherwise the caret shown up at not at the end of the content.
      (let [stored-value (get-in db value-path)]
           {:dispatch-later [(if autofocus?    {:ms 50 :fx [:elements.plain-field/focus-field! field-id]})]
            :dispatch-n     [(if on-mount      (r/metamorphic-event<-params on-mount (or initial-value stored-value)))
                             (if initial-value [:elements.plain-field/use-initial-value! field-id field-props])]})))

(r/reg-event-fx :elements.plain-field/field-will-unmount
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :value-path (Re-Frame path vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autoclear? on-unmount value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           (if autoclear? {:db         (r plain-field.events/clear-value! db field-id field-props)
                           :dispatch-n [(if on-unmount (r/metamorphic-event<-params on-unmount stored-value))]}
                          {:dispatch-n [(if on-unmount (r/metamorphic-event<-params on-unmount stored-value))]}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/use-initial-value!
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r plain-field.events/use-initial-value! db field-id field-props)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/type-ended
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-type-ended (Re-Frame metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props}]]
      ; BUG#6071 (source-code/cljs/elements/plain_field/side_effects.cljs)
      (let [field-content  (plain-field.env/get-field-content field-id)
            field-focused? (input.env/input-focused? field-id)]
           {:dispatch (if on-type-ended  (r/metamorphic-event<-params on-type-ended field-content))
            :fx       (if field-focused? [:elements.plain-field/show-surface! field-id])
            :db       (r plain-field.events/store-value! db field-id field-props field-content)})))

(r/reg-event-fx :elements.plain-field/field-blurred
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-blur (Re-Frame metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [on-blur]}]]
      {:dispatch on-blur
       :fx-n [[:elements.plain-field/hide-surface!      field-id]
              [:elements.input/unmark-input-as-focused! field-id]
              [:elements.plain-field/quit-type-mode!    field-id]]}))

(r/reg-event-fx :elements.plain-field/field-focused
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-focus (Re-Frame metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [on-focus]}]]
      {:dispatch on-focus
       :fx-n [[:elements.plain-field/show-surface!    field-id]
              [:elements.input/mark-input-as-focused! field-id]
              [:elements.plain-field/set-type-mode!   field-id]]}))
