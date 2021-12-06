
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.29
; Description:
; Version: v0.4.6
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-geometry
    (:require [mid-fruits.map        :as map]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.renderer     :as renderer]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props->popup-boxed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  ;  {:layout (keyword)(opt)}
  ;
  ; @return (boolean)
  [{:keys [layout]}]
  (= layout :boxed))

(defn popup-props->popup-stretched?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) popup-props
  ;  {:stretched? (boolean)(opt)}
  ;
  ; @return (boolean)
  [{:keys [stretched?]}]
  (boolean stretched?))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-touch-anchor?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (boolean)
  [db [_ popup-id]]
  (let [popup-props (r renderer/get-element-props db :popups popup-id)]
       (and (popup-props->popup-boxed?     popup-props)
            (popup-props->popup-stretched? popup-props)
            (r environment/touch-events-api-detected? db))))
