
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.08
; Description:
; Version: v0.6.4
; Compatibility: x4.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.scroll-indicator
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
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
  ;  {:sections (maps in vector)}
  [db [_ indicator-id]]
  (let [element-props   (r engine/get-element-view-props   db indicator-id)
        scroll-progress (r environment/get-scroll-progress db)]
       (merge (param element-props)
              {:sections [{:color :primary   :value scroll-progress}
                          {:color :highlight :value (- 100 scroll-progress)}]})))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scroll-indicator
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) indicator-id
  ; @param (map) view-props
  ;  {:shape (keyword)}
  ;
  ; @return (component)
  [indicator-id {:keys [shape] :as view-props}]
  (case shape :circle [circle-diagram/view indicator-id view-props]
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
  ;   :shape (keyword)(opt)
  ;    :circle, :line
  ;    Default: :line
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/scroll-indicator {...}]
  ;
  ; @usage
  ;  [elements/scroll-indicator :my-scroll-indicator {...}]
  ;
  ; @return (component)
  ([indicator-props]
   [view (a/id) indicator-props])

  ([indicator-id indicator-props]
   (let [indicator-props (a/prot indicator-props indicator-props-prototype)]
        [engine/stated-element indicator-id
                               {:component     #'scroll-indicator
                                :element-props indicator-props
                                :subscriber    [::get-view-props indicator-id]}])))
