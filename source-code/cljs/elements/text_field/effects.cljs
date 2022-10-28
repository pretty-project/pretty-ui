
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.effects
    (:require [elements.input.events       :as input.events]
              [elements.text-field.events  :as text-field.events]
              [elements.text-field.helpers :as text-field.helpers]
              [mid-fruits.candy            :refer [return]]
              [mid-fruits.string           :as string]
              [re-frame.api                :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/text-field-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? initial-value value-path] :as field-props}]]
      ; Az [:elements.text-field/use-initial-value! ...] esemény beállítja a kezdeti értékét
      ; az adatbázisban, majd a [:elements.text-field/hack5041 ...] esemény az adatbázisban megváltozott
      ; tárolt értéket beleírja a mezőbe.
      ;
      ; Ha az elem nem rendelkezik {:initial-value ...} tulajdonsággal és az adatbázisban a value-path
      ; útvonalon található valamilyen érték, akkor az [:elements.text-field/use-stored-value! ...]
      ; esemény a tárolt értéket beleírja a mezőbe.
      (let [stored-value (get-in db value-path)]
           {:dispatch-n [(cond initial-value [:elements.text-field/use-initial-value! field-id field-props]
                                stored-value [:elements.text-field/use-stored-value!  field-id field-props])]
            ; Az autofocus használatakor a fókuszt késleltetve teszi rá a szövegmezőre, hogy legyen ideje
            ; a mező tartalmát beállítani és a kurzor mindenképp a tartalom végén tudjon megjelenni!
            :dispatch-later [(if autofocus? {:ms 50 :fx [:elements.text-field/focus-field! field-id field-props]})]})))

(r/reg-event-fx :elements.text-field/text-field-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [autoclear?] :as field-props}]]
      {:db (as-> db % (if autoclear? (r text-field.events/clear-value! % field-id field-props)
                                     (return                           %))
                      (r input.events/unmark-as-visited! % field-id))}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  (fn [{:keys [db]} [_ field-id field-props]]
      {:db (r text-field.events/use-initial-value! db field-id field-props)}))

(r/reg-event-fx :elements.text-field/use-stored-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:value-path (vector)}
  (fn [{:keys [db]} [_ field-id {:keys [value-path] :as field-props}]]
      (let [stored-value (get-in db value-path)]
           {:fx [:elements.text-field/use-stored-value! field-id field-props stored-value]})))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/reg-keypress-events!
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
            on-esc-props   {:key-code 27 :on-keydown [:elements.text-field/ESC-pressed   field-id field-props] :required? true}]
           {:dispatch-n [[:environment/reg-keypress-event! :elements.text-field/ENTER on-enter-props]
                         [:environment/reg-keypress-event! :elements.text-field/ESC     on-esc-props]]})))

(r/reg-event-fx :elements.text-field/remove-keypress-events!
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

(r/reg-event-fx :elements.text-field/ENTER-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-enter]}]]
      {:dispatch on-enter}))

(r/reg-event-fx :elements.text-field/ESC-pressed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable?] :as field-props}]]
      (if emptiable? [:elements.text-field/empty-field! field-id field-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-empty (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      ; Az [:elements.text-field/empty-field! ...] esemény kizárólag abban az esetben
      ; törli a mező tartalmát, ha az input elem nincs disabled="true" állapotban,
      ; így elkerülhető a következő hiba:
      ; Pl. A mező on-change eseménye által indított lekérés {:disabled? true} állapotba
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
           :fx       [:elements.text-field/empty-field! field-id]
           :dispatch (if on-empty (r/metamorphic-event<-params on-empty string/empty-string))})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.text-field/type-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) field-content
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props} field-content]]
      {:db       (r text-field.events/type-ended db field-id field-props field-content)
       :dispatch (if on-type-ended (r/metamorphic-event<-params on-type-ended field-content))}))

(r/reg-event-fx :elements.text-field/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-blur (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-blur] :as field-props}]]
      {:db         (r text-field.events/field-blurred db field-id field-props)
       :dispatch-n [on-blur [:elements.text-field/remove-keypress-events! field-id field-props]]}))

(r/reg-event-fx :elements.text-field/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-focus (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [on-focus] :as field-props}]]
      {:db         (r text-field.events/field-focused db field-id field-props)
       :dispatch-n [on-focus [:elements.text-field/reg-keypress-events! field-id field-props]]}))
