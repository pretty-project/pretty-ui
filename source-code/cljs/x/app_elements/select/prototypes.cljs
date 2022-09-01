
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ;  {:border-radius (keyword)
  ;   :get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :new-option-placeholder (metamorphic-content)
  ;   :no-options-label (metamorphic-content)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:border-radius          :s
          :get-label-f            return
          :get-value-f            return
          :layout                 :select
          :new-option-f           return
          :new-option-placeholder :new-option
          :no-options-label       :no-options
          :options-path           (input.helpers/default-options-path select-id)
          :value-path             (input.helpers/default-value-path   select-id)}
         (param select-props)))
