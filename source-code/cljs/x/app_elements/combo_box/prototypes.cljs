
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.prototypes
    (:require [mid-fruits.candy             :refer [param return]]
              [x.app-elements.input.helpers :as input.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :no-options-label (metamorphic-content)
  ;   :on-blur (metamorphic-event)
  ;   :on-change (metamorphic-event)
  ;   :on-focus (metamorphic-event)
  ;   :options-path (vector)
  ;   :select-option-event (event-vector)}
  [field-id field-props]
  (merge {:get-label-f         return
          :get-value-f         return
          :no-options-label    :no-options
          :options-path        (input.helpers/default-options-path field-id)
          :select-option-event [:elements.combo-box/select-option!]}
         (param field-props)

         ; He?
         ; Ha megváltozik a mező értéke, akkor leszedi a highlight-ot!!!!
         {:on-change [:elements/combo-box-changed            field-id]}))

(defn hack3031
  [field-id field-props]
  ; HACK#3031
  ; Az on-blur és on-focus számára átadott eseményeknek szükségük van a prototípusban beállított tulajdonságokra!
  (merge field-props {:on-blur  [:elements.combo-box/remove-keypress-events! field-id field-props]
                      :on-focus [:elements.combo-box/reg-keypress-events!    field-id field-props]}))
