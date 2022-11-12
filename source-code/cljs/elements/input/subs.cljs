
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.input.subs
    (:require [mid-fruits.candy        :refer [return]]
              [elements.engine.element :as element]
              [mid-fruits.map          :refer [dissoc-in]]
              [mid-fruits.vector       :as vector]
              [re-frame.api            :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:elements.input/get-input-value :my-input {...}]
(r/reg-sub :elements.input/get-input-value get-input-value)
