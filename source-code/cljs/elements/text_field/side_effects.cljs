
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.side-effects
    (:require [elements.text-field.helpers :as text-field.helpers]
              [mid-fruits.hiccup           :as hiccup]
              [mid-fruits.string           :as string]
              [re-frame.api                :as r]
              [x.app-environment.api       :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:initial-value (string)
  ;   :field-content-f (function)}
  [field-id {:keys [initial-value field-content-f]}]
  (let [field-content (field-content-f initial-value)]
       (text-field.helpers/set-field-content! field-id field-content)))

(defn use-stored-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:field-content-f (function)}
  ; @param (string) stored-value
  [field-id {:keys [field-content-f]} stored-value]
  (let [field-content (field-content-f stored-value)]
       (text-field.helpers/set-field-content! field-id  field-content)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [field-id]
  (text-field.helpers/set-field-content! field-id string/empty-string))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (let [field-input-id (hiccup/value field-id "input")]
       (x.environment/move-caret-to-end! field-input-id)
       (x.environment/focus-element!     field-input-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/use-initial-value! use-initial-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/use-stored-value! use-stored-value!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/empty-field! empty-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.text-field/focus-field! focus-field!)
