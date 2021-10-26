
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.10
; Description:
; Version: v0.2.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.request-indicator
    (:require [mid-fruits.candy :refer [param]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-sync.api   :as sync]
              [x.app-elements.engine.api     :as engine]
              [x.app-elements.circle-diagram :as circle-diagram]
              [x.app-elements.line-diagram   :as line-diagram]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- indicator-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) indicator-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :shape (keyword)}
  [indicator-props]
  (merge {:color :primary
          :shape :line}
         (param indicator-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) indicator-id
  ;
  ; @return (map)
  ;  {:request-progress (integer)}
  [db [_ indicator-id]]
  (let [element-props    (r engine/get-element-props  db indicator-id)
        request-id       (get element-props :request-id)
        request-progress (r sync/get-request-progress db request-id)]
       (merge (param element-props)
              {:sections [{:color :primary   :value (param request-progress)}
                          {:color :highlight :value (- 100 request-progress)}]})))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) indicator-id
  ; @param (map) view-props
  ;  {:shape (keyword)}
  ;
  ; @return (component)
  [indicator-id {:keys [shape] :as view-props}]
  (case shape
        :circle [circle-diagram/view indicator-id view-props]
        :line   [line-diagram/view   indicator-id view-props]))

(defn view
  ; @param (keyword)(opt) indicator-id
  ; @param (map) indicator-props
  ;  {:class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary
  ;    Default: :primary
  ;   :diameter (px)(opt)
  ;    Default: 48
  ;    Only w/ {:shape :circle}
  ;   :request-id (keyword)
  ;   :shape (keyword)(opt)
  ;    :circle, :line
  ;    Default: :line
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/request-indicator {...}]
  ;
  ; @usage
  ;  [elements/request-indicator :my-request-indicator {...}]
  ;
  ; @return (component)
  ([indicator-props]
   [view nil indicator-props])

  ([indicator-id indicator-props]
   (let [indicator-id    (a/id   indicator-id)
         indicator-props (a/prot indicator-props indicator-props-prototype)]
        [engine/container indicator-id
          {:base-props indicator-props
           :component  request-indicator
           :subscriber [::get-view-props indicator-id]}])))
