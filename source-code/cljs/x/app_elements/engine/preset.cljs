
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.27
; Description:
; Version: v0.3.6
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.preset
    (:require [mid-fruits.candy              :refer [param return]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.field   :as field]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) presets
  ; @param (map) element-props
  ;  {:on-click (metamorphic-event)(opt)
  ;   :preset (keyword)(opt)}
  ;
  ; @usage
  ;  (engine/apply-preset {:preset-name {...}}
  ;                       {:preset :preset-name ...})
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if (some? preset)
      (let [preset-props (get presets preset)]
           (merge preset-props element-props))
      (return element-props)))
