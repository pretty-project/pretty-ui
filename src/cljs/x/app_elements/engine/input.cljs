
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.4.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.input
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [mid-fruits.random             :as random]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-elements.engine.element :as element]
              [x.app-locales.api             :as locales]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-reset-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:x.app-elements/reset-input! input-id]))

(defn default-options-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (item-path vector)
  [element-id]
  (db/path ::options element-id))

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (item-path vector)
  [element-id]
  (db/path ::values element-id))

(defn generate-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (item-path vector)
  []
  (db/path ::values (random/generate-keyword)))

(defn value-path->vector-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (item-path vector) value-path
  ;
  ; @example
  ;  (input/value-path->vector-item? [:my :value])
  ;  => false
  ;
  ; @example
  ;  (input/value-path->vector-item? [:my :value 2])
  ;  => true
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

(defn input-multilingual?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :multilingual?)))

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

(defn get-input-stored-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (*)
  [db [_ input-id]]
  ; BUG#7633 Egy input életciklusában előfordulhat olyan pillanat, amikor
  ; a value-path értéke MÉG nil. Ilyenkor ha az if-let függvény helyett let
  ; függvény deklarálná a value-path értékét, akkor abban a pillanatban
  ; a get-input-value függvény visszatérési értéke az egész adatbázis lenne.
  (if-let [value-path (r element/get-element-prop db input-id :value-path)]
          (if (r input-multilingual? db input-id)
              (r locales/get-multilingual-item db value-path)
              (get-in db value-path))))

(defn get-input-default-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (*)
  [db [_ input-id]]
  (r element/get-element-prop db input-id :default-value))

(defn get-input-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (*)
  [db [_ input-id]]
  (let [stored-value  (r get-input-stored-value  db input-id)
        default-value (r get-input-default-value db input-id)]
       (if (or (= stored-value nil)
               (= stored-value ""))
           (return default-value)
           (return stored-value))))

(a/reg-sub :x.app-elements/get-input-value get-input-value)

(defn input-empty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [input-value (r get-input-value db input-id)]
       (boolean (or (nil? input-value)
                    (= input-value "")))))

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
  (let [value (r get-input-value db input-id)
        validator (r get-input-validator db input-id)]
       ((:f validator) value)))

(defn input-value-invalid-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (or (and (r input-nonempty?           db input-id)
                    (r pre-validate-input-value? db input-id)
                    (not (r input-value-valid? db input-id)))
               (and (r input-visited?        db input-id)
                    (r validate-input-value? db input-id)
                    (not (r input-value-valid? db input-id))))))

(defn input-value-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  ;  Az input-value értéke nem NIL, FALSE vagy ""
  [db [_ input-id]]
  (let [value (r get-input-value db input-id)]
       (boolean (and (not= value nil)
                     (not= value false)
                     (not= value "")))))

(defn input-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  ;  Az input-value értéke nem NIL, FALSE vagy "" (vagy nem required),
  ;  és ha az inputot validálni kell, akkor az input-value értéke valid-e
  [db [_ input-id]]
  (boolean (and (or (r input-value-passed?        db input-id)
                    (not (r input-required?       db input-id)))
                (or (not (r validate-input-value? db input-id))
                    (r input-value-valid?         db input-id)))))

(defn input-required-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (and (r input-visited?  db input-id)
                (r input-required? db input-id)
                (not (r input-value-passed? db input-id)))))

(defn input-required-success?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (and (r input-visited?      db input-id)
                (r input-required?     db input-id)
                (r input-value-passed? db input-id))))

(defn autoclear-input?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (boolean (r element/get-element-prop db input-id :autoclear?)))

(defn get-input-view-props
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
          (assoc-in db (element/element-prop-path input-id :backup-value)
                       (get-in db value-path))
          (return db)))

(defn use-input-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if-let [initial-value   (r element/get-element-prop db input-id :initial-value)]
          (let [value-path (r element/get-element-prop db input-id :value-path)]
               (assoc-in db value-path initial-value))
          (return db)))

(defn mark-input-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (r element/set-element-prop! db input-id :visited? true))

(a/reg-event-db :x.app-elements/mark-input-as-visited! mark-input-as-visited!)

(defn reg-form-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if-let [form-id (r element/get-element-prop db input-id :form-id)]
          (r element/update-element-prop! db form-id :input-ids
             vector/conj-item input-id)
          (return db)))

(defn init-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [event-id input-id]]
  (-> db (use-input-initial-value!  [event-id input-id])
         (store-input-backup-value! [event-id input-id])
         (reg-form-input!           [event-id input-id])))

(a/reg-event-db :x.app-elements/init-input! init-input!)

(defn reset-input-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (let [backup-value (r element/get-element-prop db input-id :backup-value)
        value-path   (r element/get-element-prop db input-id :value-path)]
       (assoc-in db value-path backup-value)))

(a/reg-event-db :x.app-elements/reset-input-value! reset-input-value!)

(defn clear-input-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (let [value-path (r element/get-element-prop db input-id :value-path)]
       (dissoc-in db value-path)))

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

(defn- autoclear-input?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if (r autoclear-input?   db input-id)
      (r clear-input-value! db input-id)
      (return               db)))

(a/reg-event-db :x.app-elements/autoclear-input?! autoclear-input?!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-elements/reset-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      (let [on-reset-event (r element/get-element-prop db input-id :on-reset)]
           {:db (r reset-input-value! db input-id)
            :dispatch on-reset-event})))

(a/reg-event-fx
  :x.app-elements/reg-change-listener?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      ; WARNING#9055
      ; Az :x.app-db/reg-change-listener! esemény nem létezik!
      (let [value-path (r element/get-element-prop db input-id :value-path)])))
          ;(if (r input-listen-to-change? db input-id)
          ;    {:db (r db/reg-change-listener! value-path)})

(a/reg-event-fx
  :x.app-elements/resolve-change-listener?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  (fn [{:keys [db]} [_ input-id]]
      ; WARNING#9055
      ; Az :x.app-db/resolve-change-listener! esemény nem létezik!
      (let [value-path (r element/get-element-prop db input-id :value-path)])))
          ;(if (r input-listen-to-change? db input-id)
          ;    {:db (r db/resolve-change-listener! value-path)})
