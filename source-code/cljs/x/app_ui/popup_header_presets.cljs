
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.05
; Description:
; Version: v0.2.2
; Compatibility: x4.2.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-header-presets
    (:require [x.app-elements.api :as elements]))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-icon-button-placeholder
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button {:layout  :icon-button
                    :variant :placeholder}])

(defn popup-accept-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 13}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :accept-button
                    :indent   :right}])

(defn popup-cancel-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel-button
                    :indent   :left}])

(defn popup-up-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button {:on-click [:router/go-up!]
                    :preset   :back-icon-button}])
                   ;:tooltip  :back!

(defn popup-back-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [_ _]
  [elements/button {:on-click [:router/go-back!]
                    :preset   :back-icon-button}])
                   ;:tooltip  :back!

(defn popup-close-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :close-icon-button}])
                   ;:tooltip  :close!

(defn popup-label
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [_ {:keys [label]}]
  (if label [elements/label {:content label}]))

(defn accept-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:end-content [popup-accept-button popup-id bar-props]}])

(defn cancel-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [popup-cancel-button popup-id bar-props]}])

(defn close-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content  [popup-icon-button-placeholder popup-id bar-props]
                                 :middle-content [popup-label                   popup-id bar-props]
                                 :end-content    [popup-close-icon-button       popup-id bar-props]}])

(defn go-up-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-up-icon-button popup-id bar-props]
                                                     [popup-label          popup-id bar-props]]}])

(defn go-back-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-back-icon-button popup-id bar-props]
                                                     [popup-label            popup-id bar-props]]}])
