
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.prototypes
    (:require [mid-fruits.candy          :refer [param return]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:border-color (keyword or string)
  ;   :get-label-f (function)
  ;   :get-value-f (function)
  ;   :options-orientation (keyword)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [button-id button-props]
  (merge {:border-color        :primary
          :options-orientation :vertical
          :options-path        (engine/default-options-path button-id)
          :value-path          (engine/default-value-path   button-id)
          :get-label-f         return
          :get-value-f         return}
         (param button-props)))
