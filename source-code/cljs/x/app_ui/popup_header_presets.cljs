
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.05
; Description:
; Version: v0.3.6
; Compatibility: x4.5.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popup-header-presets
    (:require [x.app-elements.api :as elements]))



;; -- Presets -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-accept-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-accept-icon-button :my-popup {...}]
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
  ; @usage
  ;  [ui/popup-cancel-icon-button :my-popup {...}]
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :cancel-button
                    :indent   :left}])

(defn popup-go-up-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-go-up-icon-button :my-popup {...}]
  ;
  ; @return (component)
  [_ _]
  [elements/button {:on-click [:router/go-up!]
                    :preset   :back-icon-button}])

(defn popup-go-back-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-go-back-icon-button :my-popup {...}]
  ;
  ; @return (component)
  [_ _]
  [elements/button {:on-click [:router/go-back!]
                    :preset   :back-icon-button}])

(defn popup-close-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-close-icon-button :my-popup {...}]
  ;
  ; @return (component)
  [popup-id _]
  [elements/button {:keypress {:key-code 27}
                    :on-click [:ui/close-popup! popup-id]
                    :preset   :close-icon-button}])

(defn popup-placeholder-icon-button
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/popup-placeholder-icon-button :my-popup {...}]
  ;
  ; @return (component)
  [_ _]
  [elements/icon-button {:layout :icon-button :variant :placeholder}])

(defn popup-label
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/popup-label :my-popup {...}]
  ;
  ; @return (component)
  [_ {:keys [label]}]
  (if label [elements/label {:content label}]))

(defn accept-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/accept-popup-header :my-popup {...}]
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:end-content [popup-accept-button popup-id bar-props]}])

(defn cancel-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/cancel-popup-header :my-popup {...}]
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [popup-cancel-button popup-id bar-props]}])

(defn close-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;
  ; @usage
  ;  [ui/close-popup-header :my-popup {...}]
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content  [popup-placeholder-icon-button popup-id bar-props]
                                 :middle-content [popup-label                   popup-id bar-props]
                                 :end-content    [popup-close-icon-button       popup-id bar-props]}])

(defn go-up-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/go-up-popup-header :my-popup {...}]
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-go-up-icon-button popup-id bar-props]
                                                     [popup-label             popup-id bar-props]]}])

(defn go-back-popup-header
  ; @param (keyword) popup-id
  ; @param (map)(opt) bar-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [ui/go-back-popup-header :my-popup {...}]
  ;
  ; @return (component)
  [popup-id bar-props]
  [elements/horizontal-polarity {:start-content [:<> [popup-go-back-icon-button popup-id bar-props]
                                                     [popup-label               popup-id bar-props]]}])
