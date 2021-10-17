
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.02.07
; Description:
; Version: v0.2.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.api
    (:require [x.app-environment.css-handler]
              [x.app-environment.event-handler]
              [x.app-environment.mouse-handler]
              [x.app-environment.scroll-prohibitor]
              [x.app-environment.cookie-handler   :as cookie-handler]
              [x.app-environment.element-handler  :as element-handler]
              [x.app-environment.keypress-handler :as keypress-handler]
              [x.app-environment.position-handler :as position-handler]
              [x.app-environment.scroll-handler   :as scroll-handler]
              [x.app-environment.touch-handler    :as touch-handler]
              [x.app-environment.viewport-handler :as viewport-handler]
              [x.app-environment.window-handler   :as window-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-environment.cookie-handler
(def cookie-setting-path              cookie-handler/cookie-setting-path)
(def get-stored-cookies               cookie-handler/get-stored-cookies)
(def any-cookies-stored?              cookie-handler/any-cookies-stored?)
(def get-cookie-value                 cookie-handler/get-cookie-value)
(def cookies-enabled-by-browser?      cookie-handler/cookies-enabled-by-browser?)
(def analytics-cookies-enabled?       cookie-handler/analytics-cookies-enabled?)
(def necessary-cookies-enabled?       cookie-handler/necessary-cookies-enabled?)
(def user-experience-cookies-enabled? cookie-handler/user-experience-cookies-enabled?)

; x.app-environment.element-handler
(def element-id->element-disabled? element-handler/element-id->element-disabled?)

; x.app-environment.keypress-handler
(def get-pressed-keys                      keypress-handler/get-pressed-keys)
(def key-pressed?                          keypress-handler/key-pressed?)
(def enable-non-required-keypress-events!  keypress-handler/enable-non-required-keypress-events!)
(def disable-non-required-keypress-events! keypress-handler/disable-non-required-keypress-events!)

; x.app-environment.position-handler
(def get-element-position position-handler/get-element-position)

; x.app-environment.scroll-handler
(def get-scroll-direction      scroll-handler/get-scroll-direction)
(def get-scroll-y              scroll-handler/get-scroll-y)
(def scrolled-to-top?          scroll-handler/scrolled-to-top?)
(def scrolled-to-bottom?       scroll-handler/scrolled-to-bottom?)
(def scroll-direction-btt?     scroll-handler/scroll-direction-btt?)
(def scroll-direction-ttb?     scroll-handler/scroll-direction-ttb?)
(def scroll-direction-changed? scroll-handler/scroll-direction-changed?)
(def get-scroll-turn-position  scroll-handler/get-scroll-turn-position)
(def get-scroll-turn-distance  scroll-handler/get-scroll-turn-distance)
(def get-scroll-progress       scroll-handler/get-scroll-progress)

; x.app-environment.touch-handler
(def touch-events-api-detected? touch-handler/touch-events-api-detected?)

; x.app-environment.viewport-handler
(def get-viewport-height      viewport-handler/get-viewport-height)
(def get-viewport-width       viewport-handler/get-viewport-width)
(def get-viewport-profile     viewport-handler/get-viewport-profile)
(def viewport-profile-match?  viewport-handler/viewport-profile-match?)
(def viewport-profiles-match? viewport-handler/viewport-profiles-match?)
(def get-viewport-orientation viewport-handler/get-viewport-orientation)

; x.app-environment.window-handler
(def browser-online?  window-handler/browser-online?)
(def browser-offline? window-handler/browser-offline?)
