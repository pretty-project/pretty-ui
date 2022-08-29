
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.events
    (:require [mid-fruits.candy           :refer [return]]
              [mid-fruits.map             :refer [dissoc-in]]
              [mid-fruits.vector          :as vector]
              [x.app-core.api             :as a :refer [r]]
              [x.app-elements.select.subs :as select.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (map)
  [db [_ select-id {:keys [form-id initial-options initial-value options-path value-path]}]]
  ; Az init-select! függvény csak abban az esetben alkalmazza ...
  ; ... az initial-options értékét, ha az options-path útvonalon tárolt érték még üres!
  ; ... az initial-value értékét, ha a value-path útvonalon tárolt érték még üres!
  (let [options (get-in db options-path)
        value   (get-in db value-path)]
       (cond-> db (and initial-options (empty? options)) (assoc-in options-path initial-options)
                  (and initial-value   (empty? value))   (assoc-in value-path   initial-value)
                  form-id (update-in [:elements :element-handler/meta-items form-id] vector/conj-item-once select-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ _ {:keys [get-value-f value-path]} option]]
  (let [option-value (get-value-f option)]
       (assoc-in db value-path option-value)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-new-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:new-option-f (function)
  ;   :options-path (vector)}
  ; @param (string) new-option
  ;
  ; @return (map)
  [db [_ select-id {:keys [new-option-f options-path] :as select-props} new-option]]
  ; XXX#5051
  ; A new-option értéke a {:new-option? true} beállítás használatával (egy térképben)
  ; adódik a kiválasztható elemek listájához, ...
  ; ... így az elemek megjelenítésekor lehetséges megkülönböztetni az újonnan hozzáadott
  ;     elemet, azért, hogy a get-label-f függvény ne kerüljön rajta alkalmazásra, ugyanis
  ;     a hozzáadott elemen nem szükséges azt alkalmazni, mivel a h.á. elem egyszerű
  ;     string típus. Bizonyos esetekben a kiválaszható elemek is string típusúak
  ;     és mégis alkalmazva van rajtuk egy a return függvénytől különböző get-label-f függvény.
  ; ... így a kiválasztott elem mentésekor lehetséges megkülönböztetni az újonnan hozzáadott
  ;     elemet, azért, hogy a get-value-f függvény ne kerüljön rajta alkalmazásra, ...
  (if (r select.subs/new-option-exists? db select-id select-props new-option)
      (return    db)
      (update-in db options-path vector/cons-item (new-option-f new-option))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/init-element! init-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.select/clear-value! clear-value!)
