
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.input
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-locales.api             :as locales]
              [x.app-elements.engine.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#NEW VERSION!
(defn input-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:options (vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @return (vector)
  [_ {:keys [options options-path]}]
  ; XXX#2781
  ; Az egyes elemek opciói elsődlegesen a paraméterkén kapott options vektor
  ; értékei alapján kerülnek felsorolásra, annak hiányában az options-path útvonalon
  ; található értékek alapján.
  (or options @(a/subscribe [:db/get-item options-path])))




(defn on-reset-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/reset-input! input-id]))

(defn default-options-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (vector)
  [element-id]
  (db/path :elements/options element-id))

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (vector)
  [element-id]
  (db/path :elements/values element-id))

(defn value-path->vector-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) value-path
  ;
  ; @example
  ;  (input/value-path->vector-item? [:my-value])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (input/value-path->vector-item? [:my-value 2])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [value-path]
  (let [item-key (vector/last-item value-path)]
       (integer? item-key)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (some? (r element/get-element-prop db element-id :value-path)))

(defn input-required?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :required?)))

(defn input-visited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @param (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :visited?)))

(defn input-listen-to-change?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :listen-to-change?)))

; XXX#NEW VERSION!
(defn get-input-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:default-value (*)(opt)}
  ;
  ; @return (*)
  [db [_ input-id {:keys [default-value value-path] :as input-props}]]
  (let [stored-value (get-in db value-path)]
       (if (or (= stored-value nil)
               (= stored-value ""))
           (return default-value)
           (return stored-value))))

; XXX#NEW VERSION!
(defn get-input-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:options (vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @return (vector)
  [db [_ _ {:keys [options options-path]}]]
  ; XXX#2781
  (or options (get-in db options-path)))




(defn input-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [input-value (r get-input-value db input-id)]
       (or (nil? input-value)
           (=    input-value ""))))

(defn input-nonempty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r input-empty? db input-id)))

(defn input-value-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [backup-value  (r element/get-element-prop db input-id :backup-value)
        current-value (r get-input-value          db input-id)]
       (not= backup-value current-value)))

(defn get-input-validator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (r element/get-element-prop db input-id :validator))

(defn get-input-invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (metamorphic-content)
  [db [_ input-id]]
  (let [input-validator (r get-input-validator db input-id)]
       (if-let [invalid-message-f (get input-validator :invalid-message-f)]
               ; Use {:validator {:invalid-message-f ...}}
               (let [input-value (r get-input-value db input-id)]
                    (invalid-message-f input-value))
               ; Use {:validator {:invalid-message ...}}
               (get input-validator :invalid-message))))

(defn validate-input-value?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [validator (r get-input-validator db input-id)]
       (some? validator)))

(defn pre-validate-input-value?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [validator (r get-input-validator db input-id)]
       (boolean (:pre-validate? validator))))

(defn input-value-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [value     (r get-input-value     db input-id)
        validator (r get-input-validator db input-id)]
       ((:f validator) value)))

(defn input-value-invalid-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (or (and (r pre-validate-input-value? db input-id)
           (not (r input-value-valid?   db input-id))
           ; HACK#1411
           (r element/get-element-prop db input-id :initialized?))
      (and (r input-visited?            db input-id)
           (r validate-input-value?     db input-id)
           (not (r input-value-valid?   db input-id)))))

(defn input-value-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  ;  Az input-value értéke nem NIL, FALSE vagy ""
  [db [_ input-id]]
  (let [value (r get-input-value db input-id)]
       (and (not= value nil)
            (not= value false)
            (not= value ""))))

(defn input-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  ;  Az input-passed? függvény visszatérési értéke TRUE, ha az input-value
  ;  értéke nem NIL, FALSE vagy "" (vagy nem required), és ha az inputot
  ;  validálni kell, akkor az input-value értéke valid-e
  [db [_ input-id]]
  (and (or (r input-value-passed?        db input-id)
           (not (r input-required?       db input-id)))
       (or (not (r validate-input-value? db input-id))
           (r input-value-valid?         db input-id))))

(defn inputs-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keywords in vector) input-ids
  ;
  ; @return (boolean)
  ;  Az inputs-passed? függvény visszatérési értéke TRUE, ha az input-ids vektorban
  ;  felsorolt inputok értékei nem NIL, FALSE vagy "" értékek
  [db [_ input-ids]]
  (vector/all-items-match? [(last input-ids)] #(r input-passed? db %)))

(defn input-required-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [required? (r element/get-element-prop db input-id :required?)]
       (and (r input-visited?           db input-id)
            (r input-required?          db input-id)
            (not (r input-value-passed? db input-id))
            (not= :unmarked required?))))

(defn input-required-success?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (and (r input-visited?      db input-id)
       (r input-required?     db input-id)
       (r input-value-passed? db input-id)))

(defn autoclear-input?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :autoclear?)))

(defn get-input-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (map)
  ;  {:value (*)}
  [db [_ input-id]]
  {:value (r get-input-value db input-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-input-backup-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if-let [value-path (r element/get-element-prop db input-id :value-path)]
          (assoc-in db (db/path :elements/primary input-id :backup-value)
                       (get-in db value-path))
          (return db)))



; new (old) version
(defn use-initial-value!
  [db [_ input-id]]
  ; - Ha a value-path útvonalon található bármilyen érték, akkor NEM kerül felülírásra!
  ; - Az initial-value értéke lehet false, ezért szükséges a (some? ...) függvénnyel vizsgálni!
  (let [value-path     (get-in db [:elements/primary :data-items input-id :value-path])
        initial-value  (get-in db [:elements/primary :data-items input-id :initial-value])
        original-value (get-in db value-path)]
       (if (and (nil?  original-value)
                (some? initial-value))
           (assoc-in db value-path initial-value)
           (return   db))))





; XXX#NEW VERSION!
(defn use-input-initial-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:initial-options (vector)(opt)
  ;   :options-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-options options-path]}]]
  ; Az use-input-initial-options! függvény csak abban az esetben alkalmazza
  ; az initial-options értékét, ha az options-path útvonalon tárolt érték még üres!
  (let [options (get-in db options-path)]
       (cond-> db (and initial-options (empty? options))
                  (assoc-in options-path initial-options))))

; XXX#NEW VERSION!
(defn use-input-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:initial-value (*)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-value value-path]}]]
  ; Az use-input-initial-value! függvény csak abban az esetben alkalmazza
  ; az initial-value értékét, ha a value-path útvonalon tárolt érték még üres!
  (let [value (get-in db value-path)]
       (cond-> db (and initial-value (empty? value))
                  (assoc-in value-path initial-value))))

; XXX#NEW VERSION!
(defn mark-input-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (assoc-in db [:elements :element-handler/meta-items input-id :visited?] true))

(defn init-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (as-> db % ; Már nem igy van használva: lsd. select, radio-button, ...
             ;(r use-input-initial-value!  % input-id)
             (r store-input-backup-value! % input-id)

             ; HACK#1411
             ; Gyors hack, ami azt oldja meg, hogy az input validator ne validáljon addig
             ; amig az input nem kapta meg az initial-value értékét. Különben ha egy text-field
             ; validator-ja azt figyeli mondjuk, hogy üres-e a field, akkor egy pillanatra
             ; megjelenik a warning-message, mielött a field megkapja az initial-value értékét.
             ; A text-field újraírása után lehet, hogy erre a hack-re már nem is lesz szükség!
             (r element/set-element-prop! % input-id :initialized? true)))

(a/reg-event-db :elements/init-input! init-input!)

; XXX#NEW VERSION!
(defn reset-input-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (let [backup-value (get-in db [:elements :element-handler/meta-items input-id :backup-value])]
       (assoc-in db value-path backup-value)))

; XXX#NEW VERSION!
(defn clear-input-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))

(defn set-input-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ input-id value]]
  (let [value-path (r element/get-element-prop db input-id :value-path)]
       (assoc-in db value-path value)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/reset-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-reset-event (r element/get-element-prop db input-id :on-reset)]
           {:db       (r reset-input-value! db input-id)
            :dispatch on-reset-event})))

(a/reg-event-fx
  :elements/reg-change-listener?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      ; WARNING#9055
      ; Az db/reg-change-listener! esemény nem létezik!
      (let [value-path (r element/get-element-prop db input-id :value-path)])))
          ;(if (r input-listen-to-change? db input-id)
          ;    {:db (r db/reg-change-listener! value-path)})

(a/reg-event-fx
  :elements/resolve-change-listener?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      ; WARNING#9055
      ; Az db/resolve-change-listener! esemény nem létezik!
      (let [value-path (r element/get-element-prop db input-id :value-path)])))
          ;(if (r input-listen-to-change? db input-id)
          ;    {:db (r db/resolve-change-listener! value-path)})
