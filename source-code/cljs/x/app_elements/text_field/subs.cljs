
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.subs
    (:require [re-frame.api              :as r :refer [r]]
              [x.app-elements.input.subs :as input.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (= field-id (get-in db [:elements :element-handler/field-surface])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stored-value-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ field-id {:keys [validator value-path] :as field-props}]]
  (let [stored-value (get-in db value-path)]
       ((:f validator) stored-value)))

(defn stored-value-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [db [_ field-id field-props]]
  (let [stored-value-valid? (r stored-value-valid? db field-id field-props)]
       (not stored-value-valid?)))

(defn stored-value-not-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ field-id {:keys [field-content-f value-path]}]]
  (let [stored-value (get-in db value-path)]
       (-> stored-value field-content-f str empty?)))

(defn invalid-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (boolean)
  [db [_ field-id field-props]]
  (or (and (r input.subs/prevalidate-input-value? db field-id field-props)
           (r stored-value-invalid?               db field-id field-props))
      (and (r input.subs/input-visited?           db field-id field-props)
           (r input.subs/validate-input-value?    db field-id field-props)
           (r stored-value-invalid?               db field-id field-props))))

(defn required-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ field-id {:keys [required?] :as field-props}]]
  ; XXX#7551
  ; A required? értéke lehet true, false és :unmarked
  ; A {:required? :unmarked} beállítással használt input elemeken nem jelenik
  ; meg a kitöltésre figyelmeztető felirat
  (and (= required? true)
       (r input.subs/input-visited? db field-id field-props)
       (r stored-value-not-passed?  db field-id field-props)))

(defn any-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (boolean)
  [db [_ field-id field-props]]
  (or (r required-warning? db field-id field-props)
      (r invalid-warning?  db field-id field-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.text-field/surface-visible? surface-visible?)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.text-field/invalid-warning? invalid-warning?)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.text-field/required-warning? required-warning?)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.text-field/any-warning? any-warning?)
