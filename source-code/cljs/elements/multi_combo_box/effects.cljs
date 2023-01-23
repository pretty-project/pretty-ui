
(ns elements.multi-combo-box.effects
    (:require [elements.combo-box.helpers          :as combo-box.helpers]
              [elements.multi-combo-box.events     :as multi-combo-box.events]
              [elements.multi-combo-box.helpers    :as multi-combo-box.helpers]
              [elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [elements.plain-field.helpers        :as plain-field.helpers]
              [re-frame.api                        :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/reg-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      ; XXX#4156 (source-code/cljs/elements/combo_box/effects.cljs)
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)
            on-down-props  {:key-code  40 :on-keydown [:elements.combo-box/DOWN-pressed    field-id field-props] :required? true :prevent-default? true}
            on-up-props    {:key-code  38 :on-keydown [:elements.combo-box/UP-pressed      field-id field-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code  27 :on-keydown [:elements.combo-box/ESC-pressed     field-id field-props] :required? true}
            on-enter-props {:key-code  13 :on-keydown [:elements.multi-combo-box/ENTER-pressed box-id box-props] :required? true}
            on-comma-props {:key-code 188 :on-keydown [:elements.multi-combo-box/COMMA-pressed box-id box-props] :required? true :prevent-default? true}]
           {:dispatch-n [[:x.environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/COMMA on-comma-props]]})))

(r/reg-event-fx :elements.multi-combo-box/remove-keypress-events!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; XXX#4156 (source-code/cljs/elements/combo_box/effects.cljs)
  {:dispatch-n [[:x.environment/remove-keypress-event! :elements.text-field/DOWN]
                [:x.environment/remove-keypress-event! :elements.text-field/UP]
                [:x.environment/remove-keypress-event! :elements.text-field/ESC]
                [:x.environment/remove-keypress-event! :elements.text-field/ENTER]
                [:x.environment/remove-keypress-event! :elements.text-field/COMMA]]})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {}
  (fn [{:keys [db]} [_ box-id box-props]]
      ; XXX#4146 (source-code/cljs/elements/combo_box/effects.cljs)
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (plain-field.helpers/surface-visible? field-id)
               (if-let [highlighted-option (combo-box.helpers/get-highlighted-option field-id field-props)]
                       [:elements.multi-combo-box/use-option! box-id box-props highlighted-option]
                       (if (plain-field.helpers/field-empty? field-id)
                           {:fx       [:elements.plain-field/hide-surface! field-id]}
                           {:fx       [:elements.plain-field/hide-surface! field-id]
                            :dispatch [:elements.multi-combo-box/use-field-content! box-id box-props]}))
               (if (plain-field.helpers/field-filled? field-id)
                   [:elements.multi-combo-box/use-field-content! box-id box-props])))))

(r/reg-event-fx :elements.multi-combo-box/COMMA-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id (multi-combo-box.helpers/box-id->field-id box-id)]
           (if (plain-field.helpers/field-filled? field-id)
               [:elements.multi-combo-box/use-field-content! box-id box-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/use-field-content!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id      (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props   (multi-combo-box.prototypes/field-props-prototype box-id box-props)
            field-content (plain-field.helpers/get-field-content            field-id)]
           {:db       (r multi-combo-box.events/use-field-content! db box-id box-props field-content)
            :dispatch [:elements.text-field/empty-field! field-id field-props]})))

(r/reg-event-fx :elements.multi-combo-box/use-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (*) option
  (fn [{:keys [db]} [_ box-id box-props option]]
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           {:db   (r multi-combo-box.events/use-option! db box-id box-props option)
            :fx-n [[:elements.plain-field/hide-surface!             field-id]
                   [:elements.combo-box/discard-option-highlighter! field-id field-props]]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/field-changed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id _]]
      (let [field-id (multi-combo-box.helpers/box-id->field-id box-id)]
           {:fx [:elements.combo-box/discard-option-highlighter! field-id]})))

(r/reg-event-fx :elements.multi-combo-box/field-focused
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.multi-combo-box/reg-keypress-events! box-id box-props]))

(r/reg-event-fx :elements.multi-combo-box/field-blurred
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      ; TEST#0041
      ; Mivel a multi-combo-box elem használata nem egyértelmű, tehát a felhasználó
      ; számára nem látszódik, hogy nem egy text-field elemet használ, ezért a mező
      ; elhagyásakor a benne maradt érték hozzáadódik az értékek vektorához.
      ; Különben a felhasználó azt feltételezné, hogy kitölött egy mezőt és ha nem
      ; adódik hozzá az értékek vektorához a mező tartalma, akkor az adat elveszne.
      ; Szóval ez most egy UX teszt.
      (let [field-id (multi-combo-box.helpers/box-id->field-id box-id)]
           (if (plain-field.helpers/field-empty? field-id)
               {:dispatch    [:elements.multi-combo-box/remove-keypress-events! box-id box-props]}
               {:dispatch-n [[:elements.multi-combo-box/remove-keypress-events! box-id box-props]
                             [:elements.multi-combo-box/use-field-content!      box-id box-props]]}))))
