
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.prototypes
    (:require [mid-fruits.candy                   :refer [param]]
              [x.app-elements.input.helpers       :as input.helpers]
              [x.app-elements.multi-field.helpers :as multi-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:disallow-empty-input-group? (boolean)
  ;   :max-input-count (integer)
  ;   :value-path (vector)}
  [group-id group-props]
  (merge {:max-input-count 8
          :value-path (input.helpers/default-value-path group-id)}
         (param group-props)
         {:disallow-empty-input-group? true}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (map)
  ;  {}
  [group-id group-props field-dex]
  (merge (dissoc group-props :indent)
         {:autofocus?     (multi-field.helpers/field-dex->autofocus?     group-id group-props field-dex)
          :end-adornments (multi-field.helpers/field-dex->end-adornments group-id group-props field-dex)
          :label          (multi-field.helpers/field-dex->field-label    group-id group-props field-dex)
          :value-path     (multi-field.helpers/field-dex->value-path     group-id group-props field-dex)}))
