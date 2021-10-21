
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.02
; Description:
; Version: v0.2.8
; Compatibility: x4.1.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.constant-props
    (:require [mid-fruits.map :as map]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name constant-prop
;  Az elem (base-props paraméterének) azon tulajdonságai konstansak,
;  amelyek rögzítésre kerülnek az adatbázisban.
;  A XXX#0069 logika szerint az elem számára a konstans tulajdonságainak forrása
;  az adatbázis ezért azok a paraméterként átadott base-props térképet ért
;  külső hatásra nem változnak.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
;  Az elem mely tulajdonságai kerüljenek a base-props térképből a Re-Frame adatbázisba
(def CONSTANT-PROP-TYPES
     [:autoclear? :default-value :disallow-empty-input-group? :emptiable?
      :keypress :get-label-f :get-value-f :group-id :initial-value :input-ids
      :max-input-count :min-input-count :listen-to-change? :on-blur :on-change
      :on-check :on-click :on-delete :on-empty :on-enter :on-extend :on-focus
      :on-reset :on-select :on-uncheck :options-path :request-id :required?
      :validator :value-path])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn base-props->initial-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) base-props
  ;  {...}
  ;
  ; @return (map)
  ;  {...}
  [base-props]
  (map/inherit base-props CONSTANT-PROP-TYPES))
