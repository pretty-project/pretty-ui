
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
    (:require [x.app-environment.scroll-prohibitor]
              [x.app-environment.cookie-handler   :as cookie-handler]
              [x.app-environment.css-handler      :as css-handler]
              [x.app-environment.element-handler  :as element-handler]
              [x.app-environment.event-handler    :as event-handler]
              [x.app-environment.keypress-handler :as keypress-handler]
              [x.app-environment.mouse-handler    :as mouse-handler]
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

; x.app-environment.css-handler
(def add-external-css! css-handler/add-external-css!)
(def add-css!          css-handler/add-css!)
(def remove-css!       css-handler/remove-css!)

; x.app-environment.element-handler
(def element-id->element-disabled? element-handler/element-id->element-disabled?)
(def focus-element!                element-handler/focus-element!)
(def blur-element!                 element-handler/blur-element!)
(def add-element-class!            element-handler/add-element-class!)
(def remove-element-class!         element-handler/remove-element-class!)
(def set-element-style!            element-handler/set-element-style!)
(def remove-element-style!         element-handler/remove-element-style!)
(def set-element-style-value!      element-handler/set-element-style-value!)
(def remove-element-style-value!   element-handler/remove-element-style-value!)
(def set-element-attribute!        element-handler/set-element-attribute!)
(def remove-element-attribute!     element-handler/remove-element-attribute!)
(def empty-element!                element-handler/empty-element!)
(def remove-element!               element-handler/remove-element!)

; x.app-environment.event-handler
(def add-event-listener!    event-handler/add-event-listener!)
(def remove-event-listener! event-handler/remove-event-listener!)
(def add-event!             event-handler/add-event!)

; x.app-environment.keypress-handler
(def get-pressed-keys                      keypress-handler/get-pressed-keys)
(def key-pressed?                          keypress-handler/key-pressed?)
(def enable-non-required-keypress-events!  keypress-handler/enable-non-required-keypress-events!)
(def disable-non-required-keypress-events! keypress-handler/disable-non-required-keypress-events!)

; x.app-environment.mouse-handler
(def listen-to-mousemove!      mouse-handler/listen-to-mousemove!)
(def stop-listen-to-mousemove! mouse-handler/stop-listen-to-mousemove!)
(def prevent-selecting!        mouse-handler/prevent-selecting!)
(def enable-selecting!         mouse-handler/enable-selecting!)

; x.app-environment.position-handler
(def get-element-position              position-handler/get-element-position)
(def add-element-position-listener!    position-handler/add-element-position-listener!)
(def remove-element-position-listener! position-handler/remove-element-position-listener!)

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
(def set-scroll-y!             scroll-handler/set-scroll-y!)
(def scroll-to-top!            scroll-handler/scroll-to-top!)
(def scroll-to-element-top!    scroll-handler/scroll-to-element-top!)

; x.app-environment.touch-handler
(def touch-events-api-detected? touch-handler/touch-events-api-detected?)

; x.app-environment.viewport-handler
(def get-viewport-height      viewport-handler/get-viewport-height)
(def get-viewport-width       viewport-handler/get-viewport-width)
(def get-viewport-profile     viewport-handler/get-viewport-profile)
(def viewport-profile-match?  viewport-handler/viewport-profile-match?)
(def viewport-profiles-match? viewport-handler/viewport-profiles-match?)
(def viewport-small?          viewport-handler/viewport-small?)
(def viewport-medium?         viewport-handler/viewport-medium?)
(def viewport-large?          viewport-handler/viewport-large?)
(def get-viewport-orientation viewport-handler/get-viewport-orientation)

; x.app-environment.window-handler
(def browser-online?       window-handler/browser-online?)
(def browser-offline?      window-handler/browser-offline?)
(def open-new-browser-tab! window-handler/open-new-browser-tab!)
(def set-window-title!     window-handler/set-window-title!)
(def reload-window!        window-handler/reload-window!)
(def go-to-root!           window-handler/go-to-root!)
(def go-to!                window-handler/go-to!)
(def set-interval!         window-handler/set-interval!)
(def clear-interval!       window-handler/clear-interval!)
(def set-timeout!          window-handler/set-timeout!)
(def clear-timeout!        window-handler/clear-timeout!)
