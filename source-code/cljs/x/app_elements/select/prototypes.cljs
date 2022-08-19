
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.prototypes
    (:require [mid-fruits.candy          :refer [param return]]
              [x.app-elements.engine.api :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :no-options-label (metamorphic-content)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:get-label-f      return
          :get-value-f      return
          :layout           :select
          :no-options-label :no-options
          :options-path     (engine/default-options-path select-id)
          :value-path       (engine/default-value-path   select-id)}
         (param select-props)))
