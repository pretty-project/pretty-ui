
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.api
    (:require [x.app-environment.connection-handler.effects]
              [x.app-environment.connection-handler.lifecycles]
              [x.app-environment.connection-handler.side-effects]
              [x.app-environment.cookie-handler.effects]
              [x.app-environment.cookie-handler.events]
              [x.app-environment.cookie-handler.side-effects]
              [x.app-environment.element-handler.effects]
              [x.app-environment.keypress-handler.effects]
              [x.app-environment.keypress-handler.lifecycles]
              [x.app-environment.keypress-handler.side-effects]
              [x.app-environment.scroll-handler.lifecycles]
              [x.app-environment.scroll-prohibitor.effects]
              [x.app-environment.scroll-prohibitor.events]
              [x.app-environment.scroll-prohibitor.side-effects]
              [x.app-environment.scroll-prohibitor.subs]
              [x.app-environment.touch-handler.lifecycles]
              [x.app-environment.touch-handler.side-effects]
              [x.app-environment.viewport-handler.effects]
              [x.app-environment.viewport-handler.lifecycles]
              [x.app-environment.viewport-handler.side-effects]
              [x.app-environment.window-handler.lifecycles]
              [x.app-environment.connection-handler.subs      :as connection-handler.subs]
              [x.app-environment.cookie-handler.subs          :as cookie-handler.subs]
              [x.app-environment.css-handler.side-effects     :as css-handler.side-effects]
              [x.app-environment.element-handler.side-effects :as element-handler.side-effects]
              [x.app-environment.event-handler.side-effects   :as event-handler.side-effects]
              [x.app-environment.keypress-handler.events      :as keypress-handler.events]
              [x.app-environment.keypress-handler.subs        :as keypress-handler.subs]
              [x.app-environment.mouse-handler.side-effects   :as mouse-handler.side-effects]
              [x.app-environment.scroll-handler.side-effects  :as scroll-handler.side-effects]
              [x.app-environment.scroll-handler.subs          :as scroll-handler.subs]
              [x.app-environment.touch-handler.subs           :as touch-handler.subs]
              [x.app-environment.viewport-handler.subs        :as viewport-handler.subs]
              [x.app-environment.window-handler.side-effects  :as window-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-environment.connection-handler.subs
(def browser-online?  connection-handler.subs/browser-online?)
(def browser-offline? connection-handler.subs/browser-offline?)

; x.app-environment.cookie-handler.subs
(def get-stored-cookies               cookie-handler.subs/get-stored-cookies)
(def any-cookies-stored?              cookie-handler.subs/any-cookies-stored?)
(def get-cookie-value                 cookie-handler.subs/get-cookie-value)
(def cookies-enabled-by-browser?      cookie-handler.subs/cookies-enabled-by-browser?)
(def analytics-cookies-enabled?       cookie-handler.subs/analytics-cookies-enabled?)
(def necessary-cookies-enabled?       cookie-handler.subs/necessary-cookies-enabled?)
(def user-experience-cookies-enabled? cookie-handler.subs/user-experience-cookies-enabled?)

; x.app-environment.css-handler.side-effects
(def add-external-css! css-handler.side-effects/add-external-css!)
(def add-css!          css-handler.side-effects/add-css!)
(def remove-css!       css-handler.side-effects/remove-css!)

; x.app-environment.element-handler.side-effects
(def element-disabled?                     element-handler.side-effects/element-disabled?)
(def element-enabled?                      element-handler.side-effects/element-enabled?)
(def focus-element!                        element-handler.side-effects/focus-element!)
(def blur-element!                         element-handler.side-effects/blur-element!)
(def add-element-class!                    element-handler.side-effects/add-element-class!)
(def remove-element-class!                 element-handler.side-effects/remove-element-class!)
(def set-element-style!                    element-handler.side-effects/set-element-style!)
(def remove-element-style!                 element-handler.side-effects/remove-element-style!)
(def set-element-style-value!              element-handler.side-effects/set-element-style-value!)
(def remove-element-style-value!           element-handler.side-effects/remove-element-style-value!)
(def set-element-attribute!                element-handler.side-effects/set-element-attribute!)
(def remove-element-attribute!             element-handler.side-effects/remove-element-attribute!)
(def empty-element!                        element-handler.side-effects/empty-element!)
(def remove-element!                       element-handler.side-effects/remove-element!)
(def reveal-element!                       element-handler.side-effects/reveal-element!)
(def hide-element!                         element-handler.side-effects/hide-element!)
(def mark-element-masspoint-orientation!   element-handler.side-effects/mark-element-masspoint-orientation!)
(def unmark-element-masspoint-orientation! element-handler.side-effects/unmark-element-masspoint-orientation!)

; x.app-environment.event-handler.side-effects
(def add-event-listener!    event-handler.side-effects/add-event-listener!)
(def remove-event-listener! event-handler.side-effects/remove-event-listener!)
(def add-event!             event-handler.side-effects/add-event!)

; x.app-environment.keypress-handler.events
(def set-type-mode!  keypress-handler.events/set-type-mode!)
(def quit-type-mode! keypress-handler.events/quit-type-mode!)

; x.app-environment.keypress-handler.subs
(def get-pressed-keys keypress-handler.subs/get-pressed-keys)
(def key-pressed?     keypress-handler.subs/key-pressed?)

; x.app-environment.mouse-handler.side-effects
(def prevent-selecting! mouse-handler.side-effects/prevent-selecting!)
(def enable-selecting!  mouse-handler.side-effects/enable-selecting!)

; x.app-environment.scroll-handler.side-effects
(def set-scroll-y!          scroll-handler.side-effects/set-scroll-y!)
(def scroll-to-top!         scroll-handler.side-effects/scroll-to-top!)
(def scroll-to-element-top! scroll-handler.side-effects/scroll-to-element-top!)

; x.app-environment.scroll-handler.subs
(def scrolled-to-top? scroll-handler.subs/scrolled-to-top?)

; x.app-environment.touch-handler.subs
(def touch-detected? touch-handler.subs/touch-detected?)

; x.app-environment.viewport-handler.subs
(def get-viewport-height      viewport-handler.subs/get-viewport-height)
(def get-viewport-width       viewport-handler.subs/get-viewport-width)
(def get-viewport-profile     viewport-handler.subs/get-viewport-profile)
(def viewport-profile-match?  viewport-handler.subs/viewport-profile-match?)
(def viewport-profiles-match? viewport-handler.subs/viewport-profiles-match?)
(def viewport-small?          viewport-handler.subs/viewport-small?)
(def viewport-medium?         viewport-handler.subs/viewport-medium?)
(def viewport-large?          viewport-handler.subs/viewport-large?)
(def get-viewport-orientation viewport-handler.subs/get-viewport-orientation)

; x.app-environment.window-handler.side-effects
(def open-new-browser-tab! window-handler.side-effects/open-new-browser-tab!)
(def set-window-title!     window-handler.side-effects/set-window-title!)
(def reload-window!        window-handler.side-effects/reload-window!)
(def go-to-root!           window-handler.side-effects/go-to-root!)
(def go-to!                window-handler.side-effects/go-to!)
(def set-interval!         window-handler.side-effects/set-interval!)
(def clear-interval!       window-handler.side-effects/clear-interval!)
(def set-timeout!          window-handler.side-effects/set-timeout!)
(def clear-timeout!        window-handler.side-effects/clear-timeout!)
