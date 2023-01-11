
(ns elements.plain-field.effects
    (:require [elements.input.helpers       :as input.helpers]
              [elements.plain-field.events  :as plain-field.events]
              [elements.plain-field.helpers :as plain-field.helpers]
              [re-frame.api                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/field-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autofocus? (boolean)(opt)
  ;  :initial-value (*)(opt)
  ;  :on-mount (metamorphic-event)(opt)
  ;  :value-path (vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? initial-value on-mount value-path] :as field-props}]]
      ; The autofocus has to be delayed, otherwise the caret shown up at not at the end of the content.
      (let [stored-value (get-in db value-path)]
           {:dispatch-later [(if autofocus?    {:ms 50 :fx [:elements.plain-field/focus-field! field-id]})]
            :dispatch-n     [(if on-mount      (r/metamorphic-event<-params on-mount (or initial-value stored-value)))
                             (if initial-value [:elements.plain-field/use-initial-value! field-id field-props])]})))

(r/reg-event-fx :elements.plain-field/field-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:autoclear? (boolean)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :value-path (vector)}
  (fn [{:keys [db]} [_ field-id {:keys [autoclear? on-unmount value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           (if autoclear? {:db         (r plain-field.events/clear-value! db field-id field-props)
                           :dispatch-n [(if on-unmount (r/metamorphic-event<-params on-unmount stored-value))]}
                          {:dispatch-n [(if on-unmount (r/metamorphic-event<-params on-unmount stored-value))]}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r plain-field.events/use-initial-value! db field-id field-props)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.plain-field/type-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-type-ended (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props}]]
      ; BUG#6071 (source-code/cljs/elements/plain_field/side_effects.cljs)
      (let [field-content  (plain-field.helpers/get-field-content field-id)
            field-focused? (input.helpers/input-focused?         field-id)]
           {:dispatch (if on-type-ended  (r/metamorphic-event<-params on-type-ended field-content))
            :fx       (if field-focused? [:elements.plain-field/show-surface! field-id])
            :db       (r plain-field.events/store-value! db field-id field-props field-content)})))

(r/reg-event-fx :elements.plain-field/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-blur (metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [on-blur]}]]
      {:dispatch-n [on-blur]
       :fx-n       [[:elements.plain-field/hide-surface!      field-id]
                    [:elements.input/unmark-input-as-focused! field-id]
                    [:x.environment/quit-type-mode!]]}))

(r/reg-event-fx :elements.plain-field/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-focus (metamorphic-event)(opt)}
  (fn [_ [_ field-id {:keys [on-focus]}]]
      {:dispatch-n [on-focus]
       :fx-n       [[:elements.plain-field/show-surface!    field-id]
                    [:elements.input/mark-input-as-focused! field-id]
                    [:x.environment/set-type-mode!]]}))
