
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.05
; Description:
; Version: v0.2.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.surface-label-bar
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-home-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::home-icon-button
                   {:preset   :home-icon-button
                    :disabler [:x.app-router/at-home?]
                    :on-click [:x.app-router/go-home!]}])
                   ;:icon     :dashboard

(defn surface-back-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::back-icon-button
                   {:color    :primary
                    :preset   :back-icon-button
                    :on-click [:x.app-router/go-back!]}])

(defn database-browser-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::database-browser-icon-button
                   {:icon      :storage
                    :icon-size :xxs
                    :on-click  [:x.app-developer/render-database-browser!]
                    :preset    :default-icon-button
                    :tooltip   :application-database-browser}])

(defn surface-menu-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button ::menu-icon-button
                   {:preset   :menu-icon-button
                    :on-click [:x.app-views.menu/render!]}])

(defn surface-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ {:keys [label]}]
  [elements/label ::label
                  {:content label}])

(defn go-back-surface-label-bar
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;  {:label-position (keyword)(opt)
  ;    :left, :center
  ;    Default: :left}
  ;
  ; @return (component)
  [surface-id {:keys [label-position] :as bar-props}]
  [elements/polarity ::go-back-label-bar
                     {:start-content  [:<> [surface-back-icon-button surface-id bar-props]
                                           (if-not (= label-position :center)
                                                   [surface-label surface-id bar-props])]
                      :middle-content [:<> (if     (= label-position :center)
                                                   [surface-label surface-id bar-props])]
                      :end-content    [:<> (if (a/debug-mode?)
                                               [database-browser-icon-button surface-id bar-props])
                                           [surface-menu-icon-button surface-id bar-props]]}])

(defn go-home-surface-label-bar
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [surface-id bar-props]
  [elements/polarity ::go-home-label-bar
                     {:start-content [:<> [surface-home-icon-button surface-id bar-props]
                                          [surface-label            surface-id bar-props]]
                      :end-content   [:<> (if (a/debug-mode?)
                                              [database-browser-icon-button surface-id bar-props])
                                          [surface-menu-icon-button surface-id bar-props]]}])

(defn debug-surface-label-bar
  ; @param (keyword) surface-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [surface-id bar-props]
  [elements/polarity ::debug-label-bar
                     {:end-content [surface-menu-icon-button surface-id bar-props]}])
