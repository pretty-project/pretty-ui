
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.effects
    (:require [x.app-core.api                  :as a :refer [r]]
              [x.app-elements.combo-box.events :as combo-box.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      (let [on-down-props  {:key-code 40 :on-keydown [:elements.combo-box/DOWN-pressed!  field-id field-props] :required? true :prevent-default? true}
            on-up-props    {:key-code 38 :on-keydown [:elements.combo-box/UP-pressed!    field-id field-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.combo-box/ESC-pressed!   field-id field-props] :required? true}
            on-enter-props {:key-code 13 :on-keydown [:elements.combo-box/ENTER-pressed! field-id field-props] :required? true}]
           {:dispatch-n [[:environment/reg-keypress-event! ::on-DOWN-pressed  on-down-props]
                         [:environment/reg-keypress-event! ::on-UP-pressed    on-up-props]
                         [:environment/reg-keypress-event! ::on-ESC-pressed   on-esc-props]
                         [:environment/reg-keypress-event! ::on-ENTER-pressed on-enter-props]]})))

(a/reg-event-fx
  :elements.combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [_ [_ field-id field-props]]
      {:dispatch-n [[:environment/remove-keypress-event! ::on-DOWN-pressed]
                    [:environment/remove-keypress-event! ::on-UP-pressed]
                    [:environment/remove-keypress-event! ::on-ESC-pressed]
                    [:environment/remove-keypress-event! ::on-ENTER-pressed]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/DOWN-pressed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r combo-box.events/DOWN-pressed db field-id field-props)
       :fx [:elements.combo-box/highlight-next-option! field-id field-props]}))

(a/reg-event-fx
  :elements.combo-box/UP-pressed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r combo-box.events/UP-pressed db field-id field-props)
       :fx [:elements.combo-box/highlight-prev-option! field-id field-props]}))

(a/reg-event-fx
  :elements.combo-box/ESC-pressed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r combo-box.events/ESC-pressed db field-id field-props)
       :fx [:elements.combo-box/discard-option-highlighter! field-id field-props]}))

(a/reg-event-fx
  :elements.combo-box/ENTER-pressed!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r combo-box.events/ENTER-pressed db field-id field-props)
       :fx [:elements.combo-box/discard-option-highlighter! field-id field-props]}))
