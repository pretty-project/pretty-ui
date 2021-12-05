
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.2.2
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.flexible
    (:require [x.app-elements.engine.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn flexible-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:wrap-items? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-wrap-items (boolean)}
  [element-id {:keys [wrap-items?] :as element-props}]
  (cond-> (element/element-attributes element-id element-props)
          (some? wrap-items?) (assoc :data-wrap-items (boolean wrap-items?))))
