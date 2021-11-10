
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.2.0
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-adornments
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.engine.focusable :as focusable]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ;  {:icon-family (keyword)}
  [adornment-props]
  (merge {:icon-family :material-icons-filled}
         (param adornment-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-adornment-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    False érték esetén az adornment gomb nem indexelődik tabolható elemként.
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [element-id _ {:keys [icon icon-family on-click tab-indexed? tooltip]}]
  [:button.x-element--adornment-button
     ; BUG#2105
     ;  A *-field elemhez adott element-adornment-button gombon történő on-mouse-down esemény
     ;  a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
     ;  felület React-fából történő lecsatolását okozná.
    (merge {:on-mouse-down #(do (.preventDefault %))
            :on-mouse-up   #(do (a/dispatch on-click)
                                (focusable/blur-element-function element-id))
            :title            (components/content {:content tooltip})
            :data-icon-family (keyword/to-dom-value icon-family)}
           (if (false? tab-indexed?) {:tab-index "-1"}))
    (param icon)])

(defn- element-adornment-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled}
  ;
  ; @return (hiccup)
  [_ _ {:keys [icon icon-family]}]
  [:i.x-element--adornment-icon {:data-icon-family (keyword/to-dom-value icon-family)}
                                icon])

(defn- element-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) adornment-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id view-props {:keys [on-click] :as adornment-props}]
  (if (some? on-click)
      [element-adornment-button element-id view-props adornment-props]
      [element-adornment-icon   element-id view-props adornment-props]))

(defn- element-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [end-adornments] :as view-props}]
  (if (vector/nonempty? end-adornments)
      (reduce (fn [%1 %2]
                  (let [%2 (a/prot %2 adornment-props-prototype)]
                       (vector/conj-item %1 [element-adornment element-id view-props %2])))
              [:div.x-element--end-adornments] end-adornments)))

(defn- element-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ;  {:start-adornments (maps in vector)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [start-adornments] :as view-props}]
  (if (vector/nonempty? start-adornments)
      (reduce (fn [%1 %2]
                  (let [%2 (a/prot %2 adornment-props-prototype)]
                       (vector/conj-item %1 [element-adornment element-id view-props %2])))
              [:div.x-element--start-adornments] start-adornments)))
