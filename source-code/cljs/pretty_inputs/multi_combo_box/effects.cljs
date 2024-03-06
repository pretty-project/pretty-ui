
(ns pretty-inputs.multi-combo-box.effects
    (:require [pretty-inputs.combo-box.env              :as combo-box.env]
              [pretty-inputs.engine.api                 :as pretty-inputs.engine]
              [pretty-inputs.multi-combo-box.events     :as multi-combo-box.events]
              [pretty-inputs.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [pretty-inputs.text-field.env             :as text-field.env]
              [pretty-subitems.api                      :as pretty-subitems]
              [re-frame.api                             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.multi-combo-box/ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {}
  (fn [{:keys [db]} [_ box-id box-props]]
      ; XXX#4146 (source-code/cljs/pretty_inputs/combo_box/effects.cljs)
      (let [field-id    (pretty-subitems/subitem-id box-id :text-field)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (text-field.env/field-surface-visible? field-id field-props)
               (if-let [highlighted-option (combo-box.env/get-highlighted-option field-id field-props)]
                       [:pretty-inputs.multi-combo-box/use-option! box-id box-props highlighted-option]
                       (if (pretty-inputs.engine/input-empty? field-id field-props)
                           {:fx       [:pretty-inputs.text-field/hide-surface! field-id]}
                           {:fx       [:pretty-inputs.text-field/hide-surface! field-id]
                            :dispatch [:pretty-inputs.multi-combo-box/use-field-content! box-id box-props]}))
               (if (pretty-inputs.engine/input-not-empty? field-id field-props)
                   [:pretty-inputs.multi-combo-box/use-field-content! box-id box-props])))))

(r/reg-event-fx :pretty-inputs.multi-combo-box/COMMA-pressed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id    (pretty-subitems/subitem-id box-id :text-field)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (pretty-inputs.engine/input-not-empty? field-id field-props)
               [:pretty-inputs.multi-combo-box/use-field-content! box-id box-props]))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.multi-combo-box/use-field-content!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id      (pretty-subitems/subitem-id box-id :text-field)
            field-props   (multi-combo-box.prototypes/field-props-prototype box-id box-props)
            field-content (pretty-inputs.engine/get-input-displayed-value   field-id field-props)]
           {:db       (r multi-combo-box.events/use-field-content! db box-id box-props field-content)
            :dispatch [:pretty-inputs.text-field/empty-field! field-id field-props]})))

(r/reg-event-fx :pretty-inputs.multi-combo-box/use-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (*) option
  (fn [{:keys [db]} [_ box-id box-props option]]
      (let [field-id    (pretty-subitems/subitem-id box-id :text-field)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           {:db   (r multi-combo-box.events/use-option! db box-id box-props option)
            :fx-n [[:pretty-inputs.text-field/hide-surface!             field-id]
                   [:pretty-inputs.combo-box/discard-option-highlighter! field-id field-props]]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.multi-combo-box/field-changed
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id _]]
      (let [field-id (pretty-subitems/subitem-id box-id :text-field)]
           {:fx [:pretty-inputs.combo-box/discard-option-highlighter! field-id]})))

(r/reg-event-fx :pretty-inputs.multi-combo-box/field-focused
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      {:fx [:pretty-inputs.multi-combo-box/reg-keypress-events! box-id box-props]}))

(r/reg-event-fx :pretty-inputs.multi-combo-box/field-blurred
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
      (let [field-id    (pretty-subitems/subitem-id box-id :text-field)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (pretty-inputs.engine/input-empty? field-id field-props)
               {:fx       [:pretty-inputs.multi-combo-box/dereg-keypress-events! box-id box-props]}
               {:fx       [:pretty-inputs.multi-combo-box/dereg-keypress-events! box-id box-props]
                :dispatch [:pretty-inputs.multi-combo-box/use-field-content!     box-id box-props]}))))
