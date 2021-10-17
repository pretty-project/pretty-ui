
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
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.engine.focusable :as focusable]))



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  Az x4.3.9 verzióig az elemek element-end-adornments és element-start-adornments
;  komponensei az element-container komponens részeit képezték és a vertikális
;  pozícionálásuk középen volt.
;  Az x4.3.9 verzió óta a *-field elemekben a label abszolút helyett relatív
;  pozícionálású lett, ezért az input vertikális poziciója már nem középen van.
;  Azért, hogy az element-end-adornments és element-start-adornments komponensek
;  tartalma a *-field elemekben az input felett jelenjen meg, szükséges volt
;  az element-end-adornments és element-start-adornments komponenseket
;  az element-container komponens helyett a *-field elemekben elhelyezni.
;
;  Mivel az x4.3.9 verzióban kizárólag a *-field elemek alkalmaznak
;  element-end-adornments és element-start-adornments komponenseket, ezért
;  a komponensek áthelyezésekor azok CSS osztályai és más beállításai változatlanok
;  maradtak.
;  Amennyiben más elemekben is szükséges element-end-adornments és element-start-adornments
;  komponenseket alkalmazni, szükségessé válhat a CSS osztályok átnevezése és módosítása.



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-adornment-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) adornment-props
  ;  {:icon (keyword) Material icon class
  ;   :on-click (metamorphic-event)
  ;   :tooltip (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [element-id _ {:keys [icon on-click tooltip]}]
  [:button.x-element-container--adornment-button
     ; BUG#2105
     ;  A *-field elemhez adott element-adornment-button gombon történő on-mouse-down esemény
     ;  a mező on-blur eseményének triggerelésével jár, ami a mezőhöz esetlegesen használt surface
     ;  felület React-fából történő lecsatolását okozná.
    {:on-mouse-down #(do (.preventDefault %))
     :on-mouse-up   #(do (a/dispatch on-click)
                         (focusable/blur-element-function element-id))
     :title       (components/content {:content tooltip})}
    (param icon)])

(defn- element-adornment-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) view-props
  ; @param (map) adornment-props
  ;  {:icon (keyword) Material icon class}
  ;
  ; @return (hiccup)
  [_ _ {:keys [icon]}]
  [:i.x-element-container--adornment-icon icon])

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
      (reduce #(vector/conj-item %1 [element-adornment element-id view-props %2])
              [:div.x-element-container--end-adornments] end-adornments)))

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
      (reduce #(vector/conj-item %1 [element-adornment element-id view-props %2])
              [:div.x-element-container--start-adornments] start-adornments)))
