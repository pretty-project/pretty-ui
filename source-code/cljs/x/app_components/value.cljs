
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.08
; Description:
; Version: v0.6.6
; Compatibility: x4.5.7



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.value
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.random    :as random]
              [re-frame.api         :as r :refer [r]]
              [x.app-dictionary.api :as x.dictionary]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name value
;  A (metamorphic-value) típust a get-metamorphic-value subscription
;  {:value ...} tulajdonsága valósítja meg.
;  - Értéke lehet az app-dictionary szótár egy kifejezésére utaló kulcsszó
;    Pl. {:value :my-term}
;  - Értéke lehet egy egyszerű string
;    Pl. {:value "My value"}
;
; @name suffix
;  A {:suffix ...} tulajdonságként átadott szöveget toldalékaként használja.



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-metamorphic-value
  ; @param (map) value-props
  ;  {:suffix (string)(opt)
  ;   :value (keyword or string)}
  ;
  ; @usage
  ;  (r get-metamorphic-value db {...})
  ;
  ; @example
  ;  (r get-metamorphic-value db {:value :username})
  ;  =>
  ;  "Username"
  ;
  ; @example (string)
  ;  (r get-metamorphic-value db {:value "Hakuna Matata"})
  ;  =>
  ;  "Hakuna Matata"
  ;
  ; @return (string)
  [db [_ {:keys [suffix value] :as value-props}]]
  (cond ; A value-props térkép helyett a value is átadható:
        ; - (r get-metamorphic-value db {:value :username})
        ; - (r get-metamorphic-value db         :username)
        (string?  value-props) (return value-props)
        (keyword? value-props) (r x.dictionary/look-up db value-props)
        ; *
        (string?  value) (str value suffix)
        (keyword? value) (str (r x.dictionary/look-up db value) suffix)))

; @usage
;  [:components/get-metamorphic-value ...]
(r/reg-sub :components/get-metamorphic-value get-metamorphic-value)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) value-id
  ; @param (map) value-props
  ;  {:suffix (string)(opt)
  ;   :value (metamorphic-content)}
  ;
  ; @usage
  ;  (value {...})
  ;
  ; @usage
  ;  (value :my-value {...})
  ;
  ; @return (string)
  ([value-props]
   (component (random/generate-keyword) value-props))

  ([_ value-props]
   @(r/subscribe [:components/get-metamorphic-value value-props])))
