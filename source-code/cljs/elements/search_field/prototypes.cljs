
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.search-field.prototypes
    (:require [elements.text-field.helpers :as text-field.helpers]
              [mid-fruits.candy            :refer [param]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-enter (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:start-adornments (maps in vector)}
  [field-id {:keys [on-enter] :as field-props}]
  (let [field-empty? (text-field.helpers/field-empty? field-id)]
       (merge (if on-enter {:start-adornments [{:icon :search :on-click on-enter :tab-indexed? false :disabled? field-empty?}]}
                           {:start-adornments [{:icon :search}]})
              (param field-props))))