
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.input.helpers
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-locales.api             :as locales]
              [x.app-elements.engine.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-options input-id])

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-values input-id])

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
