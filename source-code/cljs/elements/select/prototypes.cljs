
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.prototypes
    (:require [candy.api              :refer [param return]]
              [elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  ;  {:add-option-f (function)
  ;   :border-radius (keyword)
  ;   :option-field-placeholder (metamorphic-content)
  ;   :layout (keyword)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :options-placeholder (metamorphic-content)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:border-radius            :s
          :add-option-f             return
          :option-field-placeholder :add!
          :option-label-f           return
          :option-value-f           return
          :layout                   :select
          :options-placeholder      :no-options
          :options-path             (input.helpers/default-options-path select-id)
          :value-path               (input.helpers/default-value-path   select-id)}
         (param select-props)))
