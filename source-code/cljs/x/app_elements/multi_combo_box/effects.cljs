
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.effects
    (:require [re-frame.api                              :as r :refer [r]]
              [x.app-elements.combo-box.helpers          :as combo-box.helpers]
              [x.app-elements.multi-combo-box.events     :as multi-combo-box.events]
              [x.app-elements.multi-combo-box.helpers    :as multi-combo-box.helpers]
              [x.app-elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [x.app-elements.text-field.events          :as text-field.events]
              [x.app-elements.text-field.helpers         :as text-field.helpers]
              [x.app-elements.text-field.subs            :as text-field.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      ; XXX#4156
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)
            on-down-props  {:key-code  40 :on-keydown [:elements.combo-box/DOWN-pressed    field-id field-props] :required? true :prevent-default? true}
            on-up-props    {:key-code  38 :on-keydown [:elements.combo-box/UP-pressed      field-id field-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code  27 :on-keydown [:elements.combo-box/ESC-pressed     field-id field-props] :required? true}
            on-enter-props {:key-code  13 :on-keydown [:elements.multi-combo-box/ENTER-pressed box-id box-props] :required? true}
            on-comma-props {:key-code 188 :on-keydown [:elements.multi-combo-box/COMMA-pressed box-id box-props] :required? true :prevent-default? true}]
           {:dispatch-n [[:environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]
                         [:environment/reg-keypress-event! :elements.text-field/COMMA on-comma-props]]})))

(r/reg-event-fx :elements.multi-combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  {:dispatch-n [[:environment/remove-keypress-event! :elements.text-field/DOWN]
                [:environment/remove-keypress-event! :elements.text-field/UP]
                [:environment/remove-keypress-event! :elements.text-field/ESC]
                [:environment/remove-keypress-event! :elements.text-field/ENTER]
                [:environment/remove-keypress-event! :elements.text-field/COMMA]]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {}
  (fn [{:keys [db]} [_ box-id box-props]]
      ; XXX#4146
      ; Ha a multi-combo-box elem surface felülete ...
      ; A) ... látható, akkor az ENTER billentyű lenyomása a multi-combo-box elem
      ;        saját működését valósítja meg.
      ; B) ... nem látható, akkor az ENTER billentyű lenyomása a text-field elem
      ;        működését valósítja meg.
      ;
      ; Ha a surface felületen ...
      ; A1) ... valamelyik opció ki van választva, akkor
      ; A2) ... egyik opció sincs kiválasztva, akkor
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (r text-field.subs/surface-visible? db field-id field-props)
               ; A)
               (if-let [highlighted-option (combo-box.helpers/get-highlighted-option field-id field-props)]
                       ; A1)
                       {:db (as-> db % (r multi-combo-box.events/use-option! % box-id box-props highlighted-option)
                                       (r text-field.events/hide-surface!    % field-id))
                        :fx [:elements.combo-box/discard-option-highlighter! field-id field-props]}
                       ; A2)
                       (if (text-field.helpers/field-empty? field-id)
                           {:db (r text-field.events/hide-surface! db field-id)}
                           (let [field-content (text-field.helpers/get-field-content field-id)]
                                {:db (as-> db % (r text-field.events/hide-surface! % field-id)
                                                (r multi-combo-box.events/use-field-content! % box-id box-props field-content))
                                 :dispatch [:elements.text-field/empty-field! field-id field-props]})))
               ; B)
               (if (text-field.helpers/field-empty? field-id)
                   ; B1)
                   [:elements.text-field/ENTER-pressed field-id field-props]
                   ; B2)
                   (let [field-content (text-field.helpers/get-field-content field-id)]
                        {:dispatch-n [[:elements.text-field/ENTER-pressed field-id field-props]
                                      [:elements.text-field/empty-field!  field-id field-props]]
                         :db (r multi-combo-box.events/use-field-content! db box-id box-props field-content)}))))))

(r/reg-event-fx :elements.multi-combo-box/COMMA-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
            field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
           (if (text-field.helpers/field-filled? field-id)
               (let [field-content (text-field.helpers/get-field-content field-id)]
                    {:db (r multi-combo-box.events/use-field-content! db box-id box-props field-content)
                     :dispatch [:elements.text-field/empty-field! field-id field-props]})))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-combo-box/field-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id _]]
      (let [field-id (multi-combo-box.helpers/box-id->field-id box-id)]
           {:fx [:elements.combo-box/discard-option-highlighter! field-id]})))

(r/reg-event-fx :elements.multi-combo-box/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.multi-combo-box/reg-keypress-events! box-id box-props]))

(r/reg-event-fx :elements.multi-combo-box/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.multi-combo-box/remove-keypress-events! box-id box-props]))
