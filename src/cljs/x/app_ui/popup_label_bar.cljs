
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.05
; Description:
; Version: v0.2.2
; Compatibility: x4.2.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-label-bar
    (:require [x.app-elements.api :as elements]))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- popup-icon-button-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button {:layout  :icon-button
                    :variant :placeholder}])

(defn- popup-accept-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 13}
                    :on-click [:x.app-ui/close-popup! popup-id]
                    :preset   :accept-button}])

(defn- popup-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:x.app-ui/close-popup! popup-id]
                    :preset   :cancel-button}])

(defn- popup-back-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button {:color    :primary
                    :on-click [:x.app-router/go-back!]
                    :preset   :back-icon-button}])

(defn- popup-close-icon-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:x.app-ui/close-popup! popup-id]
                    :preset   :close-icon-button}])

(defn- popup-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ {:keys [label]}]
  (if (some? label)
      [elements/label {:content label}]))

(defn accept-popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/polarity {:end-content [popup-accept-button popup-id bar-props]}])

(defn cancel-popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/polarity {:start-content [popup-cancel-button popup-id bar-props]}])

(defn close-popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props

  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/polarity {:start-content  [popup-icon-button-placeholder popup-id bar-props]
                      :middle-content [popup-label                   popup-id bar-props]
                      :end-content    [popup-close-icon-button       popup-id bar-props]}])

(defn go-back-popup-label-bar
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/polarity {:start-content [:<> [popup-back-icon-button popup-id bar-props]
                                          [popup-label            popup-id bar-props]]}])
