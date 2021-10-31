
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.27
; Description:
; Version: v0.3.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.preset
    (:require [mid-fruits.candy              :refer [param return]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.field   :as field]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn merge-actions
  ; @param (map) preset-props
  ;  {:on-click (metamorphic-event)(opt)}
  ; @param (map) element-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:on-click (map)
  ;   {:dispatch-n (vector)}}
  [preset-props element-props]
  (if-let [on-click (get element-props :on-click)]
          (let [preset-on-click (get preset-props :on-click)]
               {:on-click {:dispatch-n [on-click preset-on-click]}})))

(defn apply-preset
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) presets
  ; @param (map) element-props
  ;  {:on-click (metamorphic-event)(opt)
  ;   :preset (keyword)(opt)}
  ;
  ; @example
  ;  (engine/apply-preset {:preset-name {...}}
  ;                       {:preset :preset-name ...})
  ;
  ; @return (map)
  [presets {:keys [preset] :as element-props}]
  (if (some? preset)
      (let [preset-props (get presets preset)]
           (-> (param preset-props)
               (merge element-props)
               (merge (merge-actions preset-props element-props))))
      (return element-props)))
