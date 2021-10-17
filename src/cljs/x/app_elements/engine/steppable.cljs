
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.09
; Description:
; Version: v0.1.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.steppable
    (:require [mid-fruits.keyword            :as keyword]
              [x.app-core.api                :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-gestures.api            :as gestures]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-props->step-animation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) view-props
  ;  {:step-animation (keyword)(opt)
  ;   :step-direction (keyword)(opt)}
  ;
  ; @return (keyword or nil)
  [{:keys [step-animation step-direction]}]
  (if (and (some? step-animation)
           (some? step-direction))
      (keyword/join step-animation "-" step-direction)))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn steppable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) steppable-id
  ; @param (map) view-props
  ;  {:step-animation-name (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-animation (string)}
  [steppable-id view-props]
  (if-let [step-animation-name (view-props->step-animation-name view-props)]
          (element/element-attributes steppable-id view-props
                                      {:data-animation (keyword/to-dom-value step-animation-name)})
          (element/element-attributes steppable-id view-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-steppable-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) steppable-id
  ;
  ; @return (map)
  [db [_ steppable-id]]
  (r gestures/get-stepper-state db steppable-id))
