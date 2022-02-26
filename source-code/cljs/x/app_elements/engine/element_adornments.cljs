
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.2.4
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-adornments
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- adornment-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:icon-family (keyword)}
  [{:keys [icon] :as adornment-props}]
  (merge (if icon {:icon-family :material-icons-filled})
         (param adornment-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- button-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)
  ;   :on-click (metamorphic-event)
  ;   :tab-indexed? (boolean)(opt)
  ;    False érték esetén az adornment gomb nem indexelődik tabolható elemként.
  ;    Default: true
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [element-id _ {:keys [icon icon-family label on-click tab-indexed? tooltip]}]
  (let [; BUG#2105
        ;  A *-field elemhez adott element-adornment-button gombon történő on-mouse-down esemény
        ;  a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
        ;  felület React-fából történő lecsatolását okozná.
        button-attributes (merge {:on-mouse-down #(do (.preventDefault %))
                                  :on-mouse-up   #(do (a/dispatch on-click)
                                                      (environment/blur-element!))
                                  :title            (components/content tooltip)}
                                 (if (false? tab-indexed?) {:tab-index "-1"})
                                 (if icon {:data-icon-family icon-family}))]
       (cond icon  [:button.x-element--button-adornment button-attributes icon]
             label [:button.x-element--button-adornment button-attributes label])))

(defn- static-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) adornment-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :label (string)(opt)}
  ;
  ; @return (hiccup)
  [_ _ {:keys [icon icon-family label]}]
  (cond icon  [:i.x-element--static-adornment {:data-icon-family icon-family} icon]
        label [:i.x-element--static-adornment label]))

(defn- element-adornment
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (map) adornment-props
  ;  {:on-click (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id element-props {:keys [on-click] :as adornment-props}]
  (if on-click [button-adornment element-id element-props adornment-props]
               [static-adornment element-id element-props adornment-props]))

(defn- element-end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:end-adornments (maps in vector)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [end-adornments] :as element-props}]
  (if (vector/nonempty? end-adornments)
      (reduce (fn [%1 %2] (let [%2 (adornment-props-prototype %2)]
                               (conj %1 [element-adornment element-id element-props %2])))
              [:div.x-element--end-adornments] end-adornments)))

(defn- element-start-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:start-adornments (maps in vector)(opt)}
  ;
  ; @return (hiccup)
  [element-id {:keys [start-adornments] :as element-props}]
  (if (vector/nonempty? start-adornments)
      (reduce (fn [%1 %2] (let [%2 (adornment-props-prototype %2)]
                               (conj %1 [element-adornment element-id element-props %2])))
              [:div.x-element--start-adornments] start-adornments)))
