
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.04
; Description:
; Version: v0.2.0
; Compatibility: x4.4.7



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
  ;   :min-width (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-disabled (boolean)
  ;   :data-min-width (string)}
  [_ {:keys [disabled? min-width]}]
  (merge {:data-min-width (a/dom-value min-width)}
         (if (boolean disabled?) {:data-disabled true})))
