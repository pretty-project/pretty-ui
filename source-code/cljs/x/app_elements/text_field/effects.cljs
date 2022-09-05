
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.effects
    (:require [mid-fruits.string                 :as string]
              [x.app-core.api                    :as a :refer [r]]
              [x.app-elements.text-field.events  :as text-field.events]
              [x.app-elements.text-field.helpers :as text-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/init-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [initial-value value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           (cond initial-value [:elements.text-field/use-initial-value! field-id field-props]
                 stored-value  [:elements.text-field/use-stored-value!  field-id field-props]))))

(a/reg-event-fx
  :elements.text-field/use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus?] :as field-props}]]
      ; Az automatikus fókuszt késleltetve kell alkalmazni az elemen, hogy az initial-value értéknek legyen ideje
      ; az input mezőbe íródnia, különben a kurzor pozícióját nem lehetne a szöveg végére állítani.
      {:dispatch-later [               {:ms   0 :fx [:elements.text-field/use-initial-value! field-id field-props]}
                        (if autofocus? {:ms 150 :fx [:elements.text-field/focus-field!       field-id field-props]})]
       :db (r text-field.events/use-initial-value! db field-id field-props)}))

(a/reg-event-fx
  :elements.text-field/use-stored-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           {:dispatch-later [               {:ms   0 :fx [:elements.text-field/use-stored-value! field-id field-props stored-value]}
                             (if autofocus? {:ms 150 :fx [:elements.text-field/focus-field!      field-id field-props]})]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      ; XXX#4156
      ; Az :elements.text-field/ENTER és :elements.text-field/ESC azonosítók használatával más
      ; a text-field elemre épülő elemek felülírhatják a text-field elem ENTER és ESC billentyűlenyomás-figyelőit,
      ; hogy saját figyelőt állíthassanak be erre a két billentyűre. Ilyen esetben a felülíró eseményeknek
      ; kell, hogy megvalósítsák a text-field elem eredeti billentyűlenyomás-figyelő eseményeinek működését!
      (let [on-enter-props {:key-code 13 :on-keydown [:elements.text-field/ENTER-pressed field-id field-props] :required? true}
            on-esc-props   {:key-code 27 :on-keydown [:elements.combo-box/ESC-pressed    field-id field-props] :required? true}]
           {:dispatch-n [[:environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]
                         [:environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]]})))

(a/reg-event-fx
  :elements.text-field/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)
  ;   :on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable? on-enter]}]]
      ; XXX#4156
      {:dispatch-cond [on-enter   [:environment/remove-keypress-event! :elements.text-field/ENTER]
                       emptiable? [:environment/remove-keypress-event! :elements.text-field/ESC]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      {:dispatch on-enter}))

(a/reg-event-fx
  :elements.text-field/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:elements.text-field/empty-field! field-id field-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-empty (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      ; Az [:elements.text-field/empty-field! ...] esemény kizárólag abban az esetben
      ; törli a mező tartalmát, ha az input elem nincs disabled="true" állapotban,
      ; így elkerülhető a következő hiba:
      ; Pl.: A mező on-change eseménye által indított lekérés {:disabled? true} állapotba
      ;      állítja a mezőt és a szerver válaszának megérkezéséig {:disabled? true} állapotban
      ;      levő (de fókuszált) mezőt lehetséges lenne kiüríteni az ESC billentyő megnyomásának
      ;      hatására megtörténő [:elements.text-field/empty-field! ...] eseménnyel, ami miatt
      ;      megtörténne az on-empty esemény, ami az on-change eseményhez hasonlóan ismételten
      ;      elindítaná a lekérést!
      ;
      ; Ha a mező üres, akkor az [:elements.text-field/empty-field! ...] hatás nélkül történik meg,
      ; mert az üres mező esetén nem szabad ismételten megtörténnie az on-empty eseménynek.
      ; (Az értékeket pedig felesleges újra felülírni egy üres stringgel)
      ;
      ; Az on-empty esemény az on-change eseményhez hasonlóan utolsó paraméterként megkapja
      ; a mező aktuális tartalmát, ami jelen esetben egy üres string ("").
      (if (text-field.helpers/field-emptiable? field-id)
          {:db       (r text-field.events/empty-field! db field-id field-props)
           :fx       [:elements.text-field/empty-field! field-id field-props]
           :dispatch (if on-empty (a/metamorphic-event<-params on-empty string/empty-string))})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/type-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) field-content
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props} field-content]]
      {:db       (r text-field.events/type-ended db field-id field-props field-content)
       :dispatch (if on-type-ended (a/metamorphic-event<-params on-type-ended field-content))}))

(a/reg-event-fx
  :elements.text-field/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-blur (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-blur] :as field-props}]]
      {:db         (r text-field.events/field-blurred db field-id field-props)
       :dispatch-n [on-blur [:elements.text-field/remove-keypress-events! field-id field-props]]}))

(a/reg-event-fx
  :elements.text-field/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-focus (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-focus] :as field-props}]]
      {:db         (r text-field.events/field-focused db field-id field-props)
       :dispatch-n [on-focus [:elements.text-field/reg-keypress-events! field-id field-props]]}))
