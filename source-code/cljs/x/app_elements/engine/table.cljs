
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.2.2
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.table
    (:require [x.app-elements.engine.element :as element]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn table-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:horizontal-border (keyword)(opt)
  ;   :vertical-border (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-horizontal-border (keyword)
  ;   :data-vertical-border (keyword)}
  [element-id {:keys [horizontal-border vertical-border] :as element-props}]
  (cond-> (element/element-attributes element-id element-props)
          (some? horizontal-border) (assoc :data-horizontal-border horizontal-border)
          (some? vertical-border)   (assoc :data-vertical-border   vertical-border)))
