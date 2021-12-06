
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.09
; Description:
; Version: v0.1.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.steppable
    (:require [mid-fruits.keyword            :as keyword]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-gestures.api            :as gestures]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- steppable-props->step-animation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) steppable-props
  ;  {:step-animation (keyword)(opt)
  ;   :step-direction (keyword)(opt)}
  ;
  ; @return (keyword or nil)
  [{:keys [step-animation step-direction]}]
  (if (and (some? step-animation)
           (some? step-direction))
      (keyword/join step-animation "-" step-direction)))

(defn steppable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) steppable-id
  ; @param (map) steppable-props
  ;  {:step-animation-name (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-animation (string)}
  [steppable-id steppable-props]
  (if-let [step-animation-name (steppable-props->step-animation-name steppable-props)]
          (element/element-attributes steppable-id steppable-props {:data-animation step-animation-name})
          (element/element-attributes steppable-id steppable-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-steppable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) steppable-id
  ;
  ; @return (map)
  [db [_ steppable-id]]
  (r gestures/get-step-handler-state db steppable-id))
