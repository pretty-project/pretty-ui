
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.api
    (:require [x.app-ui.bubbles.effects]
              [x.app-ui.bubbles.subs]
              [x.app-ui.interface.effects]
              [x.app-ui.popups.effects]
              [x.app-ui.popups.events]
              [x.app-ui.popups.subs]
              [x.app-ui.progress-bar.effects]
              [x.app-ui.progress-screen.subs]
              [x.app-ui.sounds.subs]
              [x.app-ui.sidebar.effects]
              [x.app-ui.sidebar.events]
              [x.app-ui.sidebar.subs]
              [x.app-ui.surface.effects]
              [x.app-ui.surface.subs]
              [x.app-ui.themes.effects]
              [x.app-ui.themes.events]
              [x.app-ui.themes.lifecycles]
              [x.app-ui.title.subs]
              [x.app-ui.title.effects]
              [x.app-ui.bubbles.views               :as bubbles.views]
              [x.app-ui.error-shield.helpers        :as error-shield.helpers]
              [x.app-ui.error-shield.side-effects   :as error-shield.side-effects]
              [x.app-ui.interface.events            :as interface.events]
              [x.app-ui.interface.subs              :as interface.subs]
              [x.app-ui.loading-screen.side-effects :as loading-screen.side-effects]
              [x.app-ui.progress-bar.events         :as progress-bar.events]
              [x.app-ui.progress-bar.subs           :as progress-bar.subs]
              [x.app-ui.progress-screen.events      :as progress-screen.events]
              [x.app-ui.renderer                    :as renderer]
              [x.app-ui.sounds.side-effects         :as sounds.side-effects]
              [x.app-ui.structure.views             :as structure.views]
              [x.app-ui.themes.subs                 :as themes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.bubbles.views
(def state-changed-bubble-body bubbles.views/state-changed-bubble-body)

; x.app-ui.error-shield.helpers
(def error-shield-hidden? error-shield.helpers/error-shield-hidden?)

; x.app-ui.error-shield.side-effects
(def set-error-shield! error-shield.side-effects/set-error-shield!)

; x.app-ui.interface.events
(def set-interface! interface.events/set-interface!)
  
; x.app-ui.interface.subs
(def get-interface          interface.subs/get-interface)
(def application-interface? interface.subs/application-interface?)
(def website-interface?     interface.subs/website-interface?)

; x.app-ui.loading-screen.side-effects
(def hide-loading-screen! loading-screen.side-effects/hide-loading-screen!)

; x.app-ui.progress-bar.events
(def listen-to-process!         progress-bar.events/listen-to-process!)
(def stop-listening-to-process! progress-bar.events/stop-listening-to-process!)
(def fake-process!              progress-bar.events/fake-process!)
(def stop-faking-process!       progress-bar.events/stop-faking-process!)

; x.app-ui.progress-bar.subs
(def process-faked? progress-bar.subs/process-faked?)

; x.app-ui.progress-screen.events
(def lock-screen!   progress-screen.events/lock-screen!)
(def unlock-screen! progress-screen.events/unlock-screen!)

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
