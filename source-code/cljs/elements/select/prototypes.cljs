
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [mid-fruits.candy       :refer [param return]]))



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
  ;   :min-width (keyword)
  ;   :no-options-label (metamorphic-content)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:border-radius            :s
          :add-option-f             return
          :option-field-placeholder :add!
          :option-label-f           return
          :option-value-f           return
          :layout                   :select
          :min-width                :xxs
          :no-options-label         :no-options
          :options-path             (input.helpers/default-options-path select-id)
          :value-path               (input.helpers/default-value-path   select-id)}
         (param select-props)))
