
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.effects
    (:require [x.app-core.api                    :as a :refer [r]]
              [x.app-elements.text-field.helpers :as text-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.multi-combo-box/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ box-id box-props field-id field-props]]
      ; XXX#4156
      (let [on-down-props  {:key-code  40 :on-keydown [:elements.combo-box/DOWN-pressed        field-id field-props] :required? true :prevent-default? true}
            on-up-props    {:key-code  38 :on-keydown [:elements.combo-box/UP-pressed          field-id field-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code  27 :on-keydown [:elements.combo-box/ESC-pressed         field-id field-props] :required? true}
            on-enter-props {:key-code  13 :on-keydown [:elements.multi-combo-box/ENTER-pressed box-id box-props] :required? true}
            on-comma-props {:key-code 188 :on-keydown [:elements.multi-combo-box/COMMA-pressed box-id box-props] :required? true :prevent-default? true}]
           {:dispatch-n [[:environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]
                         [:environment/reg-keypress-event! :elements.text-field/COMMA on-comma-props]]})))

(a/reg-event-fx
  :elements.multi-combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (keyword) field-id
  ; @param (map) field-props
  {:dispatch-n [[:environment/remove-keypress-event! :elements.text-field/DOWN]
                [:environment/remove-keypress-event! :elements.text-field/UP]
                [:environment/remove-keypress-event! :elements.text-field/ESC]
                [:environment/remove-keypress-event! :elements.text-field/ENTER]
                [:environment/remove-keypress-event! :elements.text-field/COMMA]]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.multi-combo-box/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-content (text-field.helpers/get-field-content box-id)]
           (println field-content))))

(a/reg-event-fx
  :elements.multi-combo-box/COMMA-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      (let [field-content (text-field.helpers/get-field-content box-id)]
           (println box-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.multi-combo-box/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ box-id box-props field-id field-props]]
      [:elements.multi-combo-box/reg-keypress-events! box-id box-props field-id field-props]))

(a/reg-event-fx
  :elements.multi-combo-box/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ box-id box-props field-id field-props]]
      [:elements.multi-combo-box/remove-keypress-events! box-id box-props field-id field-props]))
