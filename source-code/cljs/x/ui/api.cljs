
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.api
    (:require [x.ui.bubbles.effects]
              [x.ui.bubbles.subs]
              [x.ui.interface.effects]
              [x.ui.popups.effects]
              [x.ui.popups.events]
              [x.ui.popups.subs]
              [x.ui.progress-bar.effects]
              [x.ui.progress-screen.subs]
              [x.ui.sounds.subs]
              [x.ui.sidebar.effects]
              [x.ui.sidebar.events]
              [x.ui.sidebar.subs]
              [x.ui.surface.effects]
              [x.ui.surface.subs]
              [x.ui.themes.effects]
              [x.ui.themes.events]
              [x.ui.themes.lifecycles]
              [x.ui.title.subs]
              [x.ui.title.effects]
              [x.ui.bubbles.views               :as bubbles.views]
              [x.ui.error-shield.helpers        :as error-shield.helpers]
              [x.ui.error-shield.side-effects   :as error-shield.side-effects]
              [x.ui.interface.events            :as interface.events]
              [x.ui.interface.subs              :as interface.subs]
              [x.ui.loading-screen.side-effects :as loading-screen.side-effects]
              [x.ui.progress-bar.events         :as progress-bar.events]
              [x.ui.progress-bar.subs           :as progress-bar.subs]
              [x.ui.progress-screen.events      :as progress-screen.events]
              [x.ui.renderer                    :as renderer]
              [x.ui.sounds.side-effects         :as sounds.side-effects]
              [x.ui.structure.views             :as structure.views]
              [x.ui.themes.subs                 :as themes.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.ui.bubbles.views
(def state-changed-bubble-body bubbles.views/state-changed-bubble-body)

; x.ui.error-shield.helpers
(def error-shield-hidden? error-shield.helpers/error-shield-hidden?)

; x.ui.error-shield.side-effects
(def set-error-shield! error-shield.side-effects/set-error-shield!)

; x.ui.interface.events
(def set-interface! interface.events/set-interface!)
  
; x.ui.interface.subs
(def get-interface          interface.subs/get-interface)
(def application-interface? interface.subs/application-interface?)
(def website-interface?     interface.subs/website-interface?)

; x.ui.loading-screen.side-effects
(def hide-loading-screen! loading-screen.side-effects/hide-loading-screen!)

; x.ui.progress-bar.events
(def listen-to-process!         progress-bar.events/listen-to-process!)
(def stop-listening-to-process! progress-bar.events/stop-listening-to-process!)
(def fake-process!              progress-bar.events/fake-process!)
(def stop-faking-process!       progress-bar.events/stop-faking-process!)

; x.ui.progress-bar.subs
(def process-faked? progress-bar.subs/process-faked?)

; x.ui.progress-screen.events
(def lock-screen!   progress-screen.events/lock-screen!)
(def unlock-screen! progress-screen.events/unlock-screen!)

; x.ui.renderer
(def element-rendered?      renderer/element-rendered?)
(def element-invisible?     renderer/element-invisible?)
(def element-visible?       renderer/element-visible?)
(def any-element-rendered?  renderer/any-element-rendered?)
(def any-element-invisible? renderer/any-element-invisible?)
(def any-element-visible?   renderer/any-element-visible?)
(def no-visible-elements?   renderer/no-visible-elements?)
(def set-element-prop!      renderer/set-element-prop!)

; x.ui.sounds.side-effects
(def play-sound! sounds.side-effects/play-sound!)

; x.ui.structure.views
(def structure structure.views/view)

; x.ui.themes.subs
(def get-selected-theme themes.subs/get-selected-theme)
