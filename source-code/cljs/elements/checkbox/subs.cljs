
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.checkbox.subs
    (:require [elements.input.subs :as input.subs]
              [re-frame.api        :as r :refer [r]]
              [vector.api          :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-checked?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;  {:option-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ checkbox-id {:keys [option-value-f] :as checkbox-props} option]]
  ; XXX#7234
  (let [options      (r input.subs/get-input-options db checkbox-id checkbox-props)
        stored-value (r input.subs/get-input-value   db checkbox-id checkbox-props)
        option-value (option-value-f option)]
       (if (vector/min?           options 2)
           (vector/contains-item? stored-value option-value)
           (=                     stored-value option-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.checkbox/option-checked? option-checked?)
