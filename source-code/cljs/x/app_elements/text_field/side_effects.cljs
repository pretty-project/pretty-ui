
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.side-effects
    (:require [mid-fruits.string                 :as string]
              [x.app-core.api                    :as a]
              [x.app-elements.text-field.helpers :as text-field.helpers]
              [x.app-environment.api             :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:initial-value (string)}
  [field-id {:keys [initial-value] :as field-props}]
  (text-field.helpers/set-field-value! field-id field-props initial-value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (text-field.helpers/set-field-value! field-id field-props string/empty-string))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (let [field-input-id (a/dom-value field-id "input")]
       (environment/move-caret-to-end! field-input-id)
       (environment/focus-element!     field-input-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.text-field/init-field! init-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.text-field/empty-field! empty-field!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.text-field/focus-field! focus-field!)
