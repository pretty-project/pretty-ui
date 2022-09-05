
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
  ;  {:add-option-f (function)
  ;   :border-radius (keyword)
  ;   :layout (keyword)
  ;   :new-option-placeholder (metamorphic-content)
  ;   :no-options-label (metamorphic-content)
  ;   :option-label-f (function)
  ;   :option-value-f (function)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:border-radius          :s
          :add-option-f           return
          :option-label-f         return
          :option-value-f         return
          :layout                 :select
          :new-option-placeholder :new-option
          :no-options-label       :no-options
          :options-path           (input.helpers/default-options-path select-id)
          :value-path             (input.helpers/default-value-path   select-id)}
         (param select-props)))
