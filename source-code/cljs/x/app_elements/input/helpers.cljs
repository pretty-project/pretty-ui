
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.input.helpers
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
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
  (or options @(r/subscribe [:db/get-item options-path])))

; XXX#NEW VERSION!
(defn default-options-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-options input-id])

; XXX#NEW VERSION!
(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [input-id]
  [:elements :element-handler/input-values input-id])

; XXX#NEW VERSION!
(defn value-path->vector-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) value-path
  ;
  ; @example
  ;  (value-path->vector-item? [:my-value])
  ;  =>
  ;  false
  ;
  ; @example
  ;  (value-path->vector-item? [:my-value 2])
  ;  =>
  ;  true
  ;
  ; @return (boolean)
  [value-path]
  (-> value-path last integer?))
