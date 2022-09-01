
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox.subs
    (:require [mid-fruits.vector         :as vector]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-checked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:get-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ checkbox-id {:keys [get-value-f] :as checkbox-props} option]]
  ; XXX#7234
  (let [options      (r engine/get-input-options db checkbox-id checkbox-props)
        stored-value (r engine/get-input-value   db checkbox-id checkbox-props)
        option-value (get-value-f option)]
       (if (vector/min?           options 2)
           (vector/contains-item? stored-value option-value)
           (=                     stored-value option-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.checkbox/option-checked? option-checked?)
