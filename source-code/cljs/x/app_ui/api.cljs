
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.6.0
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.api
    (:require [x.app-ui.background]
              [x.app-ui.bubbles]
              [x.app-ui.element]
              [x.app-ui.engine]
              [x.app-ui.popups]
              [x.app-ui.presets]
              [x.app-ui.shield]
              [x.app-ui.surface]
              [x.app-ui.graphics     :as graphics]
              [x.app-ui.header       :as header]
              [x.app-ui.interface    :as interface]
              [x.app-ui.progress-bar :as progress-bar]
              [x.app-ui.renderer     :as renderer]
              [x.app-ui.sounds       :as sounds]
              [x.app-ui.structure    :as structure]
              [x.app-ui.themes       :as themes]
              [x.app-ui.title        :as title]
              [x.app-ui.popup-header-presets :as popup-header-presets]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.graphics
(def app-logo          graphics/app-logo)
(def app-title         graphics/app-title)
(def loading-animation graphics/loading-animation)

; x.app-ui.header
(def set-header-title! header/set-header-title!)

; x.app-ui.interface
(def get-interface          interface/get-interface)
(def application-interface? interface/application-interface?)
(def website-interface?     interface/website-interface?)

; x.app-ui.popup-header-presets
(def popup-icon-button-placeholder popup-header-presets/popup-icon-button-placeholder)
(def popup-accept-button           popup-header-presets/popup-accept-button)
(def popup-cancel-button           popup-header-presets/popup-cancel-button)
(def popup-up-icon-button          popup-header-presets/popup-up-icon-button)
(def popup-back-icon-button        popup-header-presets/popup-back-icon-button)
(def popup-close-icon-button       popup-header-presets/popup-close-icon-button)
(def popup-label                   popup-header-presets/popup-label)
(def accept-popup-header           popup-header-presets/accept-popup-header)
(def cancel-popup-header           popup-header-presets/cancel-popup-header)
(def close-popup-header            popup-header-presets/close-popup-header)
(def go-up-popup-header            popup-header-presets/go-up-popup-header)
(def go-back-popup-header          popup-header-presets/go-back-popup-header)

; x.app-ui.progress-bar
(def listen-to-process!         progress-bar/listen-to-process!)
(def stop-listening-to-process! progress-bar/stop-listening-to-process!)

; x.app-ui.renderer
(def element-rendered?      renderer/element-rendered?)
(def element-invisible?     renderer/element-invisible?)
(def element-visible?       renderer/element-visible?)
(def any-element-rendered?  renderer/any-element-rendered?)
(def any-element-invisible? renderer/any-element-invisible?)
(def any-element-visible?   renderer/any-element-visible?)
(def no-visible-elements?   renderer/no-visible-elements?)
(def set-element-prop!      renderer/set-element-prop!)

; x.app-ui.sounds
(def play-sound! sounds/play-sound!)

; x.app-ui.structure
(def structure structure/view)

; x.app-ui.themes
(def get-selected-theme themes/get-selected-theme)
