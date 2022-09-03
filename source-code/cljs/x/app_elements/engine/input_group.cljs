
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.input-group
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.mixed              :as mixed]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name input-group
;  Olyan inputok csoportja, amelyeknek a közös value-path Re-Frame adatbázis útvonalon
;  tárolt értéke egy vektor, amelyben az egyes inputok értékei vannak.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-group-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (keyword)
  [db [_ input-id]]
  (r element/get-element-prop db input-id :group-id))

(defn disallow-empty-input-group?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Ha egy input-group rendelkezik {:disallow-empty-input-group? true}
  ; tulajdonsággal, akkor az input-group értékére való feliratkozás visszatérési
  ; értéke egy legalább egy elemű vektor.
  ; Egyes elemek (pl.: multi-field) működéséhez szükséges, hogy az input-group
  ; rendelkezzen legalább egy értékkel.
  ;
  ; @param (keyword) group-id
  ;
  ; @return (boolean)
  [db [_ group-id]]
  (boolean (r element/get-element-prop db group-id :disallow-empty-input-group?)))

(defn allow-empty-input-group?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (boolean)
  [db [_ group-id]]
  (not (r disallow-empty-input-group? db group-id)))





; XXX#NEW VERSION!
(defn max-input-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [max-input-count value-path]}]]
  (let [group-value (get-in db value-path)
        input-count (count group-value)]
       (>= input-count max-input-count)))

; XXX#NEW VERSION!
(defn get-input-group-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:disallow-empty-input-group? (boolean)(opt)}
  ;
  ; @return (vector)
  [db [_ _ {:keys [disallow-empty-input-group? initial-value value-path]}]]
  (let [group-value (get-in db value-path)]
             ; If group-value is a nonempty vector ...
       (cond (vector/nonempty? group-value)
             (return           group-value)
             ; If group-value is an empty vector & empty input-group are allowed ...
             (and (vector? group-value)
                  (not     disallow-empty-input-group?))
             (return group-value)
             ; If group-value is an empty vector & empty input-group are NOT allowed ...
             (vector? group-value)
             (return [initial-value])
             ; If group-value is NOT a vector & empty input-group are allowed ...
             (not disallow-empty-input-group?)
             (return [])
             ; If group-value is NOT a vector & empty input-group are NOT allowed ...
             :disallow-empty-input-group?
             (return [nil]))))




(defn get-input-group-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (map)
  ;  {:group-value (vector)}
  [db [_ group-id]]
  {:group-value (r get-input-group-value db group-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#NEW VERSION!
(defn conj-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:disallow-empty-input-group? (boolean)(opt)
  ;   :initial-value (*)
  ;   :value-path (vector)}
  ; @param (integer) input-dex
  ;
  ; @return (map)
  [db [_ group-id {:keys [disallow-empty-input-group? initial-value value-path]} _]]
  (let [group-value (get-in db value-path)]
             ; If group-value is a nonempty vector & allow empty input-group
             ; or group-value is a nonempty vector & disallow empty input-group ...
       (cond (vector/nonempty? group-value)
             (update-in db value-path vector/conj-item initial-value)
             ; If group-value is NOT a nonempty vector & empty input-group are NOT allowed ...
             ;
             ; Pl. Ha egy multi-field group-value értéke nil, akkor EGY field
             ;     jelenik meg, amely esetben a mezők számának növelése után
             ;     kettő darab mezőnek kell megjelenjen, amihez szükséges, hogy group-value
             ;     kettő értéket tartalmazzon.
             (boolean disallow-empty-input-group?)
             (assoc-in db value-path [initial-value initial-value])
             ; If group-value is NOT a nonempty vector & empty input-group are allowed
             (not disallow-empty-input-group?)
             (assoc-in db value-path [initial-value]))))

; XXX#NEW VERSION!
(defn increase-input-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) input-dex
  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/increase-input-count! db :my-group {:initial-value "Apple"} 1)
  ;  =>
  ;  {:my-value ["First value" "Second value" "Apple"]}
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path] :as group-props} field-dex]]
  (if (r max-input-count-reached? db group-id group-props)
      (return                db)
      (r conj-initial-value! db group-id group-props field-dex)))

; XXX#NEW VERSION!
(defn decrease-input-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {}
  ; @param (integer) input-dex
  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/decrease-input-count! db :my-group {...} 1)
  ;  =>
  ;  {:my-value ["First value"]}
  ;
  ; @return (map)
  [db [_ group-id {:keys [value-path]} input-dex]]
  (let [group-value         (get-in db value-path)
        updated-group-value (vector/remove-nth-item group-value input-dex)]
       (assoc-in db value-path updated-group-value)))






(defn stack-to-group-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (keyword) input-id
  ; @param (*) value
  ;
  ; @example
  ;  (def db {:my-group-value []})
  ;  (r engine/stack-to-group-value! db :my-group :my-input {:label "My value"})
  ;  =>
  ;  {:my-group-value [{:label "My value"}]}
  ;
  ; @example
  ;  (def db {:my-group-value ["First value" "Second value"]})
  ;  (r engine/stack-to-group-value! db :my-group :my-input "My value")
  ;  =>
  ;  {:my-group-value ["First value" "Second value" "My value"]}
  ;
  ; @return (map)
  [db [_ group-id _ value]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)]
       (update-in db group-value-path vector/conj-item-once value)))

(a/reg-event-db :elements/stack-to-group-value! stack-to-group-value!)

(defn unstack-from-group-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (keyword) input-id
  ; @param (integer) value-dex
  ;
  ; @example
  ;  (def db {:my-group-value ["First value" "Second value"]})
  ;  (r engine/unstack-from-group-value! db :my-group :my-input 0)
  ;  =>
  ;  {:my-group-value ["Second value"]}
  ;
  ; @return (map)
  [db [_ group-id _ value-dex]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)]
       (update-in db group-value-path vector/remove-nth-item value-dex)))

(a/reg-event-db :elements/unstack-from-group-value! unstack-from-group-value!)
