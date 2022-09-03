
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.input.subs
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-locales.api             :as locales]
              [x.app-elements.engine.element :as element]))



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

; XXX#NEW VERSION!
(defn input-visited?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @param (boolean)
  [db [_ input-id _]]
  (get-in db [:elements :element-handler/meta-items input-id :visited?]))

; XXX#NEW VERSION!
(defn input-focused?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @param (boolean)
  [db [_ input-id _]]
  (get-in db [:elements :element-handler/meta-items input-id :focused?]))

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
  ;  {:default-value (*)(opt)
  ;   :value-path (vector)}
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
  [db [_ input-id]])
  ;(let [input-value (r get-input-value db input-id)]
  ;     (or (nil? input-value)
  ;         (=    input-value "")]])

(defn input-nonempty?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]])
  ;(not (r input-empty? db input-id)))

(defn value-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]])
  ;(let [backup-value  (r element/get-element-prop db input-id :backup-value)
  ;      current-value (r get-input-value          db input-id)
  ;     (not= backup-value current-value)]])

(defn get-input-validator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (r element/get-element-prop db input-id :validator))

(defn get-invalid-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (metamorphic-content)
  [db [_ input-id]])
  ;(let [input-validator (r get-input-validator db input-id)]
  ;     (if-let [invalid-message-f (get input-validator :invalid-message-f)]
  ;             ; Use {:validator {:invalid-message-f ...}}
  ;             (let [input-value (r get-input-value db input-id)]
  ;                  (invalid-message-f input-value)
  ;             ; Use {:validator {:invalid-message ...}}
  ;             (get input-validator :invalid-message)]])

; XXX#NEW VERSION!
(defn validate-input-value?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id {:keys [validator]}]]
  (some? validator))

; XXX#NEW VERSION!
(defn prevalidate-input-value?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id {:keys [validator]}]]
  (:prevalidate? validator))

; XXX#NEW VERSION!
(defn input-value-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id {:keys [validator] :as input-props}]]
  (let [value (r get-input-value db input-id input-props)]
       ((:f validator) value)))

; XXX#NEW VERSION!
(defn input-value-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (let [value-valid? (r input-value-valid? db input-id input-props)]
       (not value-valid?)))

; XXX#NEW VERSION!
(defn invalid-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  [db [_ input-id {:keys [] :as input-props}]]
  (or (and (r prevalidate-input-value? db input-id input-props)
           (r input-value-invalid?     db input-id input-props))
      (and (r input-visited?           db input-id input-props)
           (r validate-input-value?    db input-id input-props)
           (r input-value-invalid?     db input-id input-props))))

; XXX#NEW VERSION!
(defn input-value-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;
  ; @return (boolean)
  ;  A input-value-passed? függvény visszatérési értéke true, ha a input-value
  ;  értéke nem NIL, FALSE vagy ""
  [db [_ input-id input-props]]
  (let [input-value (r get-input-value db input-id input-props)]
       (and (not= input-value nil)
            (not= input-value false)
            (not= input-value ""))))

(defn input-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  ;  Az input-passed? függvény visszatérési értéke TRUE, ha az input-value
  ;  értéke nem NIL, FALSE vagy "" (vagy nem required), és ha az inputot
  ;  validálni kell, akkor az input-value értéke valid-e
  [db [_ input-id]])
  ;(and (or (r input-value-passed?        db input-id)
  ;         (not (r input-required?       db input-id))
  ;     (or (not (r validate-input-value? db input-id))
  ;         (r input-value-valid?         db input-id)]])

(defn inputs-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keywords in vector) input-ids
  ;
  ; @return (boolean)
  ;  Az inputs-passed? függvény visszatérési értéke TRUE, ha az input-ids vektorban
  ;  felsorolt inputok értékei nem NIL, FALSE vagy "" értékek
  [db [_ input-ids]])
  ;(vector/all-items-match? [(last input-ids)] #(r input-passed? db %)))

; XXX#NEW VERSION!
(defn required-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id {:keys [required?] :as input-props}]]
  ; A required? értéke lehet true, false és :unmarked
  ; A {:required? :unmarked} beállítással használt input elemeken nem jelenik
  ; meg a kitöltésre figyelmeztető felirat
  (and (= required? true)
       (r input-visited?           db input-id input-props)
       (not (r input-value-passed? db input-id input-props))))

; XXX#NEW VERSION!
(defn any-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ input-id input-props]]
  (or (r required-warning? db input-id input-props)
      (r invalid-warning?  db input-id input-props)))

(defn required-success?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]])
  ;(and (r input-visited?      db input-id)
  ;     (r input-required?     db input-id)
  ;     (r input-value-passed? db input-id)]])

(defn autoclear-input?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]])
  ;(boolean (r element/get-element-prop db input-id :autoclear?)))

(defn get-input-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:value (*)}
  [db [_ input-id]])
  ;{:value (r get-input-value db input-id)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.input/invalid-warning? invalid-warning?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.input/required-warning? required-warning?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.input/any-warning? any-warning?)
