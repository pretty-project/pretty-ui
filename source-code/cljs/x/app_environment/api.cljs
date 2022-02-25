
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.api
    (:require [x.app-environment.scroll-prohibitor]
              [x.app-environment.cookie-handler.effects]
              [x.app-environment.cookie-handler.engine]
              [x.app-environment.cookie-handler.events]
              [x.app-environment.cookie-handler.side-effects]
              [x.app-environment.css-handler.engine]
              [x.app-environment.cookie-handler.subs      :as cookie-handler.subs]
              [x.app-environment.css-handler.side-effects :as css-handler.side-effects]
              [x.app-environment.element-handler          :as element-handler]
              [x.app-environment.event-handler            :as event-handler]
              [x.app-environment.keypress-handler         :as keypress-handler]
              [x.app-environment.mouse-handler            :as mouse-handler]
              [x.app-environment.scroll-handler           :as scroll-handler]
              [x.app-environment.touch-handler            :as touch-handler]
              [x.app-environment.viewport-handler         :as viewport-handler]
              [x.app-environment.window-handler           :as window-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

; x.app-environment.element-handler
(def element-disabled?           element-handler/element-disabled?)
(def element-enabled?            element-handler/element-enabled?)
(def focus-element!              element-handler/focus-element!)
(def blur-element!               element-handler/blur-element!)
(def add-element-class!          element-handler/add-element-class!)
(def remove-element-class!       element-handler/remove-element-class!)
(def set-element-style!          element-handler/set-element-style!)
(def remove-element-style!       element-handler/remove-element-style!)
(def set-element-style-value!    element-handler/set-element-style-value!)
(def remove-element-style-value! element-handler/remove-element-style-value!)
(def set-element-attribute!      element-handler/set-element-attribute!)
(def remove-element-attribute!   element-handler/remove-element-attribute!)
(def empty-element!              element-handler/empty-element!)
(def remove-element!             element-handler/remove-element!)
(def reveal-element!             element-handler/reveal-element!)
(def hide-element!               element-handler/hide-element!)
(def mark-element-masspoint-orientation!   element-handler/mark-element-masspoint-orientation!)
(def unmark-element-masspoint-orientation! element-handler/unmark-element-masspoint-orientation!)

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
(def prevent-selecting! mouse-handler/prevent-selecting!)
(def enable-selecting!  mouse-handler/enable-selecting!)

; x.app-environment.scroll-handler
(def scrolled-to-top?       scroll-handler/scrolled-to-top?)
(def set-scroll-y!          scroll-handler/set-scroll-y!)
(def scroll-to-top!         scroll-handler/scroll-to-top!)
(def scroll-to-element-top! scroll-handler/scroll-to-element-top!)

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
(def interval-exists?      window-handler/interval-exists?)
(def timeout-exists?       window-handler/timeout-exists?)
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
