
(ns elements.multi-combo-box.effects
    (:require [elements.combo-box.env              :as combo-box.env]
              [elements.multi-combo-box.events     :as multi-combo-box.events]
              [elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [elements.multi-combo-box.utils      :as multi-combo-box.utils]
              [elements.plain-field.env            :as plain-field.env]
              [re-frame.api                        :as r :refer [r]]))

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
      (let [field-id    (multi-combo-box.utils/box-id->field-id           box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (plain-field.env/surface-visible? field-id)
               (if-let [highlighted-option (combo-box.env/get-highlighted-option field-id field-props)]
                       [:elements.multi-combo-box/use-option! box-id box-props highlighted-option]
                       (if (plain-field.env/field-empty? field-id)
                           {:fx       [:elements.plain-field/hide-surface! field-id]}
                           {:fx       [:elements.plain-field/hide-surface! field-id]
                            :dispatch [:elements.multi-combo-box/use-field-content! box-id box-props]}))
               (if (plain-field.env/field-filled? field-id)
                   [:elements.multi-combo-box/use-field-content! box-id box-props])))))

(r/reg-event-fx :elements.multi-combo-box/COMMA-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id (multi-combo-box.utils/box-id->field-id box-id)]
           (if (plain-field.env/field-filled? field-id)
               [:elements.multi-combo-box/use-field-content! box-id box-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/use-field-content!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id      (multi-combo-box.utils/box-id->field-id           box-id)
            field-props   (multi-combo-box.prototypes/field-props-prototype box-id box-props)
            field-content (plain-field.env/get-field-content                field-id)]
           {:db       (r multi-combo-box.events/use-field-content! db box-id box-props field-content)
            :dispatch [:elements.text-field/empty-field! field-id field-props]})))

(r/reg-event-fx :elements.multi-combo-box/use-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (*) option
  (fn [{:keys [db]} [_ box-id box-props option]]
      (let [field-id    (multi-combo-box.utils/box-id->field-id           box-id)
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
      (let [field-id (multi-combo-box.utils/box-id->field-id box-id)]
           {:fx [:elements.combo-box/discard-option-highlighter! field-id]})))

(r/reg-event-fx :elements.multi-combo-box/field-focused
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx [:elements.multi-combo-box/reg-keypress-events! box-id box-props]}))

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
      (let [field-id (multi-combo-box.utils/box-id->field-id box-id)]
           (if (plain-field.env/field-empty? field-id)
               {:fx       [:elements.multi-combo-box/remove-keypress-events! box-id box-props]}
               {:fx       [:elements.multi-combo-box/remove-keypress-events! box-id box-props]
                :dispatch [:elements.multi-combo-box/use-field-content!      box-id box-props]}))))
