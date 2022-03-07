
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.api
    (:require [x.app-ui.background.views]
              [x.app-ui.bubbles.effects]
              [x.app-ui.bubbles.engine]
              [x.app-ui.bubbles.subs]
              [x.app-ui.element]
              [x.app-ui.engine]
              [x.app-ui.header.subs]
              [x.app-ui.header.views]
              [x.app-ui.interface.effects]
              [x.app-ui.interface.events]
              [x.app-ui.interface.lifecycles]
              [x.app-ui.locker.views]
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
              [x.app-ui.sounds.engine]
              [x.app-ui.sounds.subs]
              [x.app-ui.sounds.views]
              [x.app-ui.surface.effects]
              [x.app-ui.surface.engine]
              [x.app-ui.surface.views]
              [x.app-ui.themes.effects]
              [x.app-ui.themes.events]
              [x.app-ui.themes.lifecycles]
              [x.app-ui.title.subs]
              [x.app-ui.title.effects]
              [x.app-ui.bubbles.views       :as bubbles.views]
              [x.app-ui.graphics.views      :as graphics.views]
              [x.app-ui.header.events       :as header.events]
              [x.app-ui.interface.subs      :as interface.subs]
              [x.app-ui.progress-bar.events :as progress-bar.events]
              [x.app-ui.renderer            :as renderer]
              [x.app-ui.sounds.side-effects :as sounds.side-effects]
              [x.app-ui.structure.views     :as structure.views]
              [x.app-ui.themes.subs         :as themes.subs]
              [x.app-ui.popups.views        :as popups.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.bubbles.views
(def state-changed-bubble-body bubbles.views/state-changed-bubble-body)

; x.app-ui.graphics.views
(def app-logo          graphics.views/app-logo)
(def app-title         graphics.views/app-title)
(def loading-animation graphics.views/loading-animation)

; x.app-ui.header.events
(def set-header-title!    header.events/set-header-title!)
(def remove-header-title! header.events/remove-header-title!)

; x.app-ui.interface.subs
(def get-interface          interface.subs/get-interface)
(def application-interface? interface.subs/application-interface?)
(def website-interface?     interface.subs/website-interface?)

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

; x.app-ui.sounds.side-effects
(def play-sound! sounds.side-effects/play-sound!)

; x.app-ui.structure.views
(def structure structure.views/view)

; x.app-ui.themes.subs
(def get-selected-theme themes.subs/get-selected-theme)
