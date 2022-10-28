
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.events
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :refer [r]]
              [x.app-db.api      :as x.db]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-value-f (function)
  ;   :value-path (vector)}
  ; @param (*) selected-option
  ;
  ; @return (map)
  [db [_ _ {:keys [option-value-f value-path]} selected-option]]
  (let [option-value (option-value-f selected-option)]
       (r x.db/apply-item! db value-path vector/conj-item-once option-value)))

(defn use-field-content!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:field-value-f (function)
  ;   :value-path (vector)}
  ; @param (*) field-content
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]} field-content]]
  (let [field-value (field-value-f field-content)]
       (r x.db/apply-item! db value-path vector/conj-item-once field-value)))
