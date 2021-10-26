
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.4.8
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.api
    (:require [x.app-ui.background]
              [x.app-ui.bubbles]
              [x.app-ui.canvas]
              [x.app-ui.element]
              [x.app-ui.engine]
              [x.app-ui.notification-sounds]
              [x.app-ui.popups]
              [x.app-ui.presets]
              [x.app-ui.process-bar]
              [x.app-ui.shield]
              [x.app-ui.sidebar]
              [x.app-ui.surface]
              [x.app-ui.graphics          :as graphics]
              [x.app-ui.interface         :as interface]
              [x.app-ui.popup-label-bar   :as popup-label-bar]
              [x.app-ui.renderer          :as renderer]
              [x.app-ui.surface-geometry  :as surface-geometry]
              [x.app-ui.surface-label-bar :as surface-label-bar]
              [x.app-ui.structure         :as structure]
              [x.app-ui.themes            :as themes]
              [x.app-ui.title             :as title]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.graphics
(def app-logo            graphics/app-logo)
(def app-title           graphics/app-title)
(def loading-animation-a graphics/loading-animation-a)
(def loading-animation-b graphics/loading-animation-b)
(def loading-animation-c graphics/loading-animation-c)
(def loading-animation-d graphics/loading-animation-d)
(def success-animation-a graphics/success-animation-a)
(def failure-animation-a graphics/failure-animation-a)

; x.app-ui.interface
(def get-interface          interface/get-interface)
(def application-interface? interface/application-interface?)
(def website-interface?     interface/website-interface?)

; x.app-ui.popup-label-bar
(def accept-popup-label-bar  popup-label-bar/accept-popup-label-bar)
(def cancel-popup-label-bar  popup-label-bar/cancel-popup-label-bar)
(def close-popup-label-bar   popup-label-bar/close-popup-label-bar)
(def go-back-popup-label-bar popup-label-bar/go-back-popup-label-bar)

; x.app-ui.renderer
(def element-rendered?      renderer/element-rendered?)
(def element-invisible?     renderer/element-invisible?)
(def element-visible?       renderer/element-visible?)
(def any-element-rendered?  renderer/any-element-rendered?)
(def any-element-invisible? renderer/any-element-invisible?)
(def any-element-visible?   renderer/any-element-visible?)
(def no-visible-elements?   renderer/no-visible-elements?)
(def set-element-prop!      renderer/set-element-prop!)

; x.app-ui.structure
(def structure structure/view)

; x.app-ui.surface-geometry
(def get-surface-header-height surface-geometry/get-surface-header-height)

; x.app-ui.surface-label-bar
(def go-back-surface-label-bar surface-label-bar/go-back-surface-label-bar)
(def go-home-surface-label-bar surface-label-bar/go-home-surface-label-bar)
(def debug-surface-label-bar   surface-label-bar/debug-surface-label-bar)

; x.app-ui.themes
(def get-selected-theme themes/get-selected-theme)
