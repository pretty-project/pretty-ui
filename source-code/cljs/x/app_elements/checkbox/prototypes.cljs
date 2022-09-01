
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.checkbox.prototypes
    (:require [mid-fruits.candy          :refer [param return]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn checkbox-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :font-size (keyword)
  ;   :get-label-f (function)
  ;   :get-value-f (function)
  ;   :options-orientation (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [checkbox-id checkbox-props]
  (merge {:border-color        :primary
          :font-size           :s
          :get-label-f         return
          :get-value-f         return
          :options-orientation :vertical
          :options-path        (engine/default-options-path checkbox-id)
          :value-path          (engine/default-value-path   checkbox-id)}
         (param checkbox-props)))
