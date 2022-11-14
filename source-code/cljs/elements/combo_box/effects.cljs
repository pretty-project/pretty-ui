
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.combo-box.effects
    (:require [elements.combo-box.events  :as combo-box.events]
              [elements.combo-box.helpers :as combo-box.helpers]
              [elements.text-field.events :as text-field.events]
              [elements.text-field.subs   :as text-field.subs]
              [re-frame.api               :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/reg-keypress-events!
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
      ; hogy az elnevezések konzisztensek maradjanak.
      (let [on-down-props  {:key-code 40 :on-keydown [:elements.combo-box/DOWN-pressed  box-id box-props] :required? true :prevent-default? true}
            on-up-props    {:key-code 38 :on-keydown [:elements.combo-box/UP-pressed    box-id box-props] :required? true :prevent-default? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.combo-box/ESC-pressed   box-id box-props] :required? true}
            on-enter-props {:key-code 13 :on-keydown [:elements.combo-box/ENTER-pressed box-id box-props] :required? true}]
           {:dispatch-n [[:x.environment/reg-keypress-event! :elements.text-field/DOWN   on-down-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/UP       on-up-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]
                         [:x.environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]]})))

(r/reg-event-fx :elements.combo-box/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; XXX#4156
  {:dispatch-n [[:x.environment/remove-keypress-event! :elements.text-field/DOWN]
                [:x.environment/remove-keypress-event! :elements.text-field/UP]
                [:x.environment/remove-keypress-event! :elements.text-field/ESC]
                [:x.environment/remove-keypress-event! :elements.text-field/ENTER]]})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/DOWN-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:db (r text-field.events/show-surface!       db box-id)
       :fx [:elements.combo-box/highlight-next-option! box-id box-props]}))

(r/reg-event-fx :elements.combo-box/UP-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:db (r text-field.events/show-surface!       db box-id)
       :fx [:elements.combo-box/highlight-prev-option! box-id box-props]}))

(r/reg-event-fx :elements.combo-box/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      ; Ha a combo-box elem surface felülete ...
      ; A) ... megjelenít opciókat, akkor az ESC billentyű lenyomása a combo-box
      ;        elem saját működését valósítja meg.
      ; B) ... nem jelenít meg opciókat, akkor az ESC billentyű lenyomása a text-field
      ;        elem működését valósítja meg.
      ;
      ; HACK#1450 (source-code/cljs/elements/combo-box/helpers.cljs)
      (if (combo-box.helpers/any-option-rendered? box-id box-props)
          ; A)
          {:db (r text-field.events/hide-surface!            db box-id)
           :fx [:elements.combo-box/discard-option-highlighter! box-id]}
          ; B)
          [:elements.text-field/ESC-pressed box-id box-props])))

(r/reg-event-fx :elements.combo-box/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      ; XXX#4146
      ; Ha a combo-box elem surface felülete ...
      ; A) ... látható, akkor az ENTER billentyű lenyomása a combo-box elem
      ;        saját működését valósítja meg.
      ; B) ... nem látható, akkor az ENTER billentyű lenyomása a text-field elem
      ;        működését valósítja meg.
      ;
      ; Ha a surface felületen ...
      ; A1) ... valamelyik opció ki van választva, akkor a kiválasztott opciót,
      ;         eltárolja a value-path útvonalra és a szövegmező értékének is beállítja.
      ; A2) ... egyik opció sincs kiválasztva, akkor eltünteti a surface felületet.
      (if (r text-field.subs/surface-visible? db box-id box-props)
          ; A)
          (if-let [highlighted-option (combo-box.helpers/get-highlighted-option box-id box-props)]
                  ; A1)
                  {:db   (as-> db % (r combo-box.events/select-option! % box-id box-props highlighted-option)
                                    (r text-field.events/hide-surface! % box-id))
                   :fx-n [[:elements.combo-box/discard-option-highlighter! box-id]
                          [:elements.combo-box/use-selected-option!        box-id box-props highlighted-option]]}
                  ; A2)
                  {:db   (r text-field.events/hide-surface! db box-id)})
          ; B)
          [:elements.text-field/ENTER-pressed box-id box-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; @param (*) selected-option
  (fn [{:keys [db]} [_ box-id box-props selected-option]]
      {:db (as-> db % (r combo-box.events/select-option! % box-id box-props selected-option)
                      (r text-field.events/hide-surface! % box-id))
       :fx-n [[:elements.combo-box/discard-option-highlighter! box-id]
              [:elements.combo-box/use-selected-option!        box-id box-props selected-option]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.combo-box/field-changed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [{:keys [db]} [_ box-id box-props]]
      {:fx [:elements.combo-box/discard-option-highlighter! box-id]}))

(r/reg-event-fx :elements.combo-box/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/reg-keypress-events! box-id box-props]))

(r/reg-event-fx :elements.combo-box/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  (fn [_ [_ box-id box-props]]
      [:elements.combo-box/remove-keypress-events! box-id box-props]))
