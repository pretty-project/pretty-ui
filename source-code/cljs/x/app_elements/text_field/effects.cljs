
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
  (fn [{:keys [db]} [_ field-id {:keys [autofocus? initial-value] :as field-props}]]
      ; Az automatikus fókuszt késleltetve kell alkalmazni az elemen, hogy az initial-value értéknek legyen ideje
      ; az input mezőbe íródnia, különben a kurzor pozícióját nem lehetne a szöveg végére állítani.
      {:dispatch-later [(if initial-value {:ms   0 :fx [:elements.text-field/init-field!  field-id field-props]})
                        (if autofocus?    {:ms 150 :fx [:elements.text-field/focus-field! field-id field-props]})]
       :db   (as-> db % (if initial-value (r text-field.events/init-field! % field-id field-props)))}))

(a/reg-event-fx
  :elements.text-field/reg-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)
  ;   :on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable? on-enter] :as field-props}]]
      {:dispatch-cond [on-enter   (let [on-enter-props  {:key-code 13 :required? true :on-keydown on-enter}]
                                       [:environment/reg-keypress-event! ::on-ENTER-pressed on-enter-props])
                       emptiable? (let [on-escape       [:elements.text-field/empty-field! field-id field-props]
                                        on-escape-props {:key-code 27 :required? true :on-keydown on-escape}]
                                       [:environment/reg-keypress-event! ::on-ESC-pressed   on-escape-props])]}))

(a/reg-event-fx
  :elements.text-field/remove-keypress-events!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:emptiable? (boolean)(opt)
  ;   :on-enter (metamorphic-event)(opt)}
  (fn [{:keys [db]} [_ field-id {:keys [emptiable? on-enter]}]]
      {:dispatch-cond [on-enter   [:environment/remove-keypress-event! ::on-ENTER-pressed]
                       emptiable? [:environment/remove-keypress-event! ::on-ESC-pressed]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements.text-field/empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [on-empty] :as field-props}]]
      ; - Az [:elements.text-field/empty-field! ...] esemény kizárólag abban az esetben
      ;   törli a mező tartalmát, ha az input elem nincs disabled="true" állapotban,
      ;   így elkerülhető a következő hiba:
      ;   Pl.: A mező on-changed eseménye által indított request disabled="true" állapotba
      ;        állítja a mezőt, és a szerver válaszának megérkezéséig disabled="true" állapotban
      ;        levő (de fókuszált) mezőt lehetséges lenne kiüríteni az ESC billentyő megnyomásával,
      ;        ami ismételten elindítaná a request-et (az on-empty esemény által)!
      ;
      ; - Ha a mező üres, akkor az [:elements.text-field/empty-field! ...] hatás nélkül történik meg,
      ;   mert az üres mező esetén nem szabad ismételten megtörténnie az on-empty eseménynek.
      ;   (Az értékeket pedig felesleges újra felülírni egy üres stringgel)
      ;
      ; - Az on-empty esemény az on-changed eseményhez hasonlóan utolsó paraméterként megkapja
      ;   a mező aktuális tartalmát ("")
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
  ; @param (string) value
  (fn [{:keys [db]} [_ field-id {:keys [on-type-ended] :as field-props} value]]
      {:db       (r text-field.events/type-ended db field-id field-props value)
       :dispatch (if on-type-ended (a/metamorphic-event<-params on-type-ended value))}))

(a/reg-event-fx
  :elements.text-field/field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [on-blur] :as field-props}]]
      {:db         (r text-field.events/field-blurred db field-id field-props)
       :dispatch-n [on-blur [:elements.text-field/remove-keypress-events! field-id field-props]]}))

(a/reg-event-fx
  :elements.text-field/field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  (fn [{:keys [db]} [_ field-id {:keys [on-focus] :as field-props}]]
      {:db         (r text-field.events/field-focused db field-id field-props)
       :dispatch-n [on-focus [:elements.text-field/reg-keypress-events! field-id field-props]]}))
