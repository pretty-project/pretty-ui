
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.effects
    (:require [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.combo-box.events  :as combo-box.events]
              [x.app-elements.combo-box.helpers :as combo-box.helpers]
              [x.app-elements.text-field.events :as text-field.events]
              [x.app-elements.text-field.subs   :as text-field.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      ; XXX#4156
      ; Az :elements.text-field/ESC és :elements.text-field/ENTER azonosítók használatával
      ; a combo-box elem által regisztrált billentyűlenyomás-figyelők felülírják a text-field
      ; elem ESC és ENTER billentyűlenyomás-figyelőit, hogy azok működése ne zavarja a combo-box
      ; elem működését.
      ; A felülírt események eredeti funkcionalitását a combo-box elem billentyűlenyomás-figyelő
      ; eseményei természetesen megvalósítják ...
      ;
      ; Az UP és DOWN billentyűlenyomás-figyelők az említett másik két eseményhez hasonlóan
      ; :elements.text-field/... azonosítót kapnak :elements.combo-box/... azonosító helyett,
      ; így az elnevezések konzisztensek maradhatnak.
      (let [on-down-props  {:key-code 40 :on-keydown [:elements.combo-box/DOWN-pressed  box-id box-props] :required? true :prevent-default? true}
            on-up-props    {:key-code 38 :on-keydown [:elements.combo-box/UP-pressed    box-id box-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.combo-box/ESC-pressed   box-id box-props] :required? true}
            on-enter-props {:key-code 13 :on-keydown [:elements.combo-box/ENTER-pressed box-id box-props] :required? true}]
           {:dispatch-n [[:environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]]})))

(a/reg-event-fx
  :elements.combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; XXX#4156
  {:dispatch-n [[:environment/remove-keypress-event! :elements.text-field/DOWN]
                [:environment/remove-keypress-event! :elements.text-field/UP]
                [:environment/remove-keypress-event! :elements.text-field/ESC]
                [:environment/remove-keypress-event! :elements.text-field/ENTER]]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/DOWN-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:db (r text-field.events/show-surface!       db box-id box-props)
       :fx [:elements.combo-box/highlight-next-option! box-id box-props]}))

(a/reg-event-fx
  :elements.combo-box/UP-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:db (r text-field.events/show-surface!       db box-id box-props)
       :fx [:elements.combo-box/highlight-prev-option! box-id box-props]}))

(a/reg-event-fx
  :elements.combo-box/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      ; Ha a combo-box elem surface felülete ...
      ; A) ... látható, akkor az ESC billentyű lenyomása a combo-box elem
      ;        saját működését valósítja meg.
      ; B) ... nem látható, akkor az ESC billentyű lenyomása a text-field elem
      ;        működését valósítja meg.
      (if (r text-field.subs/surface-visible? db box-id box-props)
          ; A)
          {:db (r text-field.events/hide-surface!            db box-id box-props)
           :fx [:elements.combo-box/discard-option-highlighter! box-id box-props]}
          ; B)
          [:elements.text-field/ESC-pressed box-id box-props])))

(a/reg-event-fx
  :elements.combo-box/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      ; Ha a combo-box elem surface felülete ...
      ; A) ... látható, akkor az ENTER billentyű lenyomása a combo-box elem
      ;        saját működését valósítja meg.
      ; B) ... nem látható, akkor az ENTER billentyű lenyomása a text-field elem
      ;        működését valósítja meg.
      (if (r text-field.subs/surface-visible? db box-id box-props)
          ; A)
          (if-let [highlighted-option (combo-box.helpers/get-highlighted-option box-id box-props)]
                  {:db   (as-> db % (r combo-box.events/select-option! % box-id box-props highlighted-option)
                                    (r text-field.events/hide-surface! % box-id box-props))
                   :fx-n [[:elements.combo-box/discard-option-highlighter! box-id box-props]
                          [:elements.combo-box/use-selected-option!        box-id box-props highlighted-option]]}
                  {:db   (r text-field.events/hide-surface! db box-id box-props)})
          ; B)
          [:elements.text-field/ENTER-pressed box-id box-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (*) selected-option
  (fn [{:keys [db]} [_ box-id box-props selected-option]]
      {:db (as-> db % (r combo-box.events/select-option! % box-id box-props selected-option)
                      (r text-field.events/hide-surface! % box-id box-props))
       :fx-n [[:elements.combo-box/discard-option-highlighter! box-id box-props]
              [:elements.combo-box/use-selected-option!        box-id box-props selected-option]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.combo-box/field-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:fx [:elements.combo-box/discard-option-highlighter! box-id box-props]}))

(a/reg-event-fx
  :elements.combo-box/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/reg-keypress-events! box-id box-props]))

(a/reg-event-fx
  :elements.combo-box/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/remove-keypress-events! box-id box-props]))