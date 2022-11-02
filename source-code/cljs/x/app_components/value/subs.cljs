
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.value.subs
    (:require [mid-fruits.candy     :refer [return]]
              [re-frame.api         :as r :refer [r]]
              [x.app-dictionary.api :as x.dictionary]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-metamorphic-value
  ; @param (map) value-props
  ;  {:prefix (string)(opt)
  ;   :replacements (numbers or strings in vector)(opt)
  ;   :suffix (string)(opt)
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
  [db [_ {:keys [prefix replacements suffix value] :as value-props}]]
  ; A value-props térkép helyett a value tulajdonság értéke is átadható:
  ; Pl.: (r get-metamorphic-value db {:value :username})
  ;      (r get-metamorphic-value db         :username)
  (if (map? value-props)
      (cond (string?  value) (x.dictionary/join-string value prefix suffix replacements)
            (keyword? value) (let [value       (r x.dictionary/look-up db value)
                                   value-props (assoc value-props :value value)]
                                  (r get-metamorphic-value db value-props)))
      (r get-metamorphic-value db {:value value-props})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:components/get-metamorphic-value ...]
(r/reg-sub :components/get-metamorphic-value get-metamorphic-value)
