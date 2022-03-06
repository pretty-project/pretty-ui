
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.api
    (:require [x.app-ui.background.views]
              [x.app-ui.bubbles]
              [x.app-ui.element]
              [x.app-ui.engine]
              [x.app-ui.header.subs]
              [x.app-ui.header.views]
              [x.app-ui.popups.effects]
              [x.app-ui.popups.engine]
              [x.app-ui.popups.events]
              [x.app-ui.popups.subs]
              [x.app-ui.popups.views]
              [x.app-ui.presets]
              [x.app-ui.progress-bar.effects]
              [x.app-ui.progress-bar.subs]
              [x.app-ui.progress-bar.views]
              [x.app-ui.shield]
              [x.app-ui.surface.effects]
              [x.app-ui.surface.engine]
              [x.app-ui.surface.views]
              [x.app-ui.title.subs]
              [x.app-ui.title.effects]
              [x.app-ui.graphics             :as graphics]
              [x.app-ui.header.events        :as header.events]
              [x.app-ui.interface            :as interface]
              [x.app-ui.progress-bar.events  :as progress-bar.events]
              [x.app-ui.renderer             :as renderer]
              [x.app-ui.sounds               :as sounds]
              [x.app-ui.structure.views      :as structure.views]
              [x.app-ui.themes               :as themes]
              [x.app-ui.bubble-body-presets  :as bubble-body-presets]
              [x.app-ui.popups.views         :as popups.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.bubble-body-presets
(def state-changed-bubble-body bubble-body-presets/state-changed-bubble-body)

; x.app-ui.graphics
(def app-logo          graphics/app-logo)
(def app-title         graphics/app-title)
(def loading-animation graphics/loading-animation)

; x.app-ui.header.events
(def set-header-title!    header.events/set-header-title!)
(def remove-header-title! header.events/remove-header-title!)

; x.app-ui.interface
(def get-interface          interface/get-interface)
(def application-interface? interface/application-interface?)
(def website-interface?     interface/website-interface?)

; x.app-ui.popups.views
(def popup-accept-button           popups.views/popup-accept-button)
(def popup-cancel-button           popups.views/popup-cancel-button)
(def popup-go-up-icon-button       popups.views/popup-go-up-icon-button)
(def popup-go-back-icon-button     popups.views/popup-go-back-icon-button)
(def popup-close-icon-button       popups.views/popup-close-icon-button)
(def popup-placeholder-icon-button popups.views/popup-label)
(def popup-label                   popups.views/popup-label)
(def accept-popup-header           popups.views/accept-popup-header)
(def cancel-popup-header           popups.views/cancel-popup-header)
(def close-popup-header            popups.views/close-popup-header)
(def go-up-popup-header            popups.views/go-up-popup-header)
(def go-back-popup-header          popups.views/go-back-popup-header)

; x.app-ui.progress-bar.events
(def listen-to-process!         progress-bar.events/listen-to-process!)
(def stop-listening-to-process! progress-bar.events/stop-listening-to-process!)
(def fake-process!              progress-bar.events/fake-process!)
(def stop-faking-process!       progress-bar.events/stop-faking-process!)

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

; x.app-ui.structure.views
(def structure structure.views/view)

; x.app-ui.themes
(def get-selected-theme themes/get-selected-theme)
