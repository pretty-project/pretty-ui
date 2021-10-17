
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.29
; Description:
; Version: v0.4.6
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-geometry
    (:require [mid-fruits.candy      :refer [param]]
              [mid-fruits.map        :as map]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.renderer     :as renderer]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name extended-surface-header
;  A scrollup esemény hatására a surface-header felületen az állandóan megjelenő
;  label-bar alatt a control-bar is megjelenik.
;
; @name compact-surface-header
;  A scrolldown esemény hatására a surface-header felületen az állandóan megjelenő
;  label-bar alatt a control-bar eltűnik.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (px)
(def SURFACE-HEADER-DEFAULT-HEIGHT 0)

; @constant (px)
(def SURFACE-HEADER-BORDER-HEIGHT 2)

; @constant (px)
(def SURFACE-CONTROL-BAR-HEIGHT 48)

; @constant (px)
(def SURFACE-LABEL-BAR-HEIGHT 48)

; @constant (px)
(def SURFACE-CONTROL-SIDEBAR-WIDTH 250)



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-props->render-control-sidebar?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;  {:control-sidebar (map)(opt)}
  ;
  ; @return (boolean)
  [{:keys [control-sidebar]}]
  (map/nonempty? control-sidebar))

(defn surface-props->render-control-bar?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;  {:control-bar (map)(opt)}
  ;
  ; @return (boolean)
  [{:keys [control-bar]}]
  (map/nonempty? control-bar))

(defn surface-props->render-label-bar?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;  {:label-bar (map)(opt)}
  ;
  ; @return (boolean)
  [{:keys [label-bar]}]
  (map/nonempty? label-bar))

(defn surface-props->render-surface-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (boolean)
  [surface-props]
  (or (surface-props->render-control-bar? surface-props)
      (surface-props->render-label-bar?   surface-props)))

(defn surface-props->extended-surface-header-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (integer)
  [surface-props]
  (cond-> (param SURFACE-HEADER-DEFAULT-HEIGHT)
          ; *
          (surface-props->render-label-bar? surface-props)
          (+ SURFACE-LABEL-BAR-HEIGHT)
          ; *
          (surface-props->render-control-bar? surface-props)
          (+ SURFACE-CONTROL-BAR-HEIGHT)
          ; *
          (or (surface-props->render-label-bar?   surface-props)
              (surface-props->render-control-bar? surface-props))
          (+ SURFACE-HEADER-BORDER-HEIGHT)))

(defn surface-props->compact-surface-header-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (integer)
  [surface-props]
  (cond-> (param SURFACE-HEADER-DEFAULT-HEIGHT)
          ; *
          (surface-props->render-label-bar? surface-props)
          (+ SURFACE-LABEL-BAR-HEIGHT)
          ; *
          (or (surface-props->render-label-bar?   surface-props)
              (surface-props->render-control-bar? surface-props))
          (+ SURFACE-HEADER-BORDER-HEIGHT)))

(defn surface-props->surface-structure-padding-top
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) surface-props
  ;
  ; @return (integer)
  [surface-props]
  (surface-props->extended-surface-header-height surface-props))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-header-extended?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [scroll-direction-ttb? (r environment/scroll-direction-ttb? db)]
       (not scroll-direction-ttb?)))

(defn get-surface-header-height
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ;
  ; @return (integer)
  [db [_ surface-id]]
  (let [surface-props            (r renderer/get-element-props db :surface surface-id)
        surface-header-extended? (r surface-header-extended?   db)]
       (if (boolean surface-header-extended?)
           (surface-props->extended-surface-header-height surface-props)
           (surface-props->compact-surface-header-height  surface-props))))
