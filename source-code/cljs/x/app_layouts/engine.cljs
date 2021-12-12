
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.04
; Description:
; Version: v0.2.4
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.engine
    (:require [x.app-core.api :as a]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:disabled? (boolean)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;   :min-width (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-disabled (boolean)
  ;   :data-horizontal-align (keyword)
  ;   :data-min-width (keyword)}
  [_ {:keys [disabled? horizontal-align min-width]}]
  (cond-> {} (some? min-width)        (assoc :data-min-width        min-width)
             (some? horizontal-align) (assoc :data-horizontal-align horizontal-align)
             (some? disabled?)        (assoc :data-disabled         disabled?)))
