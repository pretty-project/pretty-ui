
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.api
    (:require [x.environment.connection-handler.effects]
              [x.environment.connection-handler.lifecycles]
              [x.environment.connection-handler.side-effects]
              [x.environment.cookie-handler.effects]
              [x.environment.cookie-handler.events]
              [x.environment.cookie-handler.lifecycles]
              [x.environment.cookie-handler.side-effects]
              [x.environment.keypress-handler.effects]
              [x.environment.keypress-handler.lifecycles]
              [x.environment.keypress-handler.side-effects]
              [x.environment.scroll-handler.lifecycles]
              [x.environment.scroll-prohibitor.effects]
              [x.environment.scroll-prohibitor.events]
              [x.environment.scroll-prohibitor.side-effects]
              [x.environment.scroll-prohibitor.subs]
              [x.environment.touch-handler.lifecycles]
              [x.environment.touch-handler.side-effects]
              [x.environment.viewport-handler.effects]
              [x.environment.viewport-handler.lifecycles]
              [x.environment.viewport-handler.side-effects]
              [x.environment.window-handler.lifecycles]
              [x.environment.connection-handler.subs      :as connection-handler.subs]
              [x.environment.cookie-handler.subs          :as cookie-handler.subs]
              [x.environment.css-handler.side-effects     :as css-handler.side-effects]
              [x.environment.element-handler.side-effects :as element-handler.side-effects]
              [x.environment.event-handler.side-effects   :as event-handler.side-effects]
              [x.environment.keypress-handler.events      :as keypress-handler.events]
              [x.environment.keypress-handler.subs        :as keypress-handler.subs]
              [x.environment.mouse-handler.side-effects   :as mouse-handler.side-effects]
              [x.environment.observe-handler.side-effects :as observe-handler.side-effects]
              [x.environment.scroll-handler.side-effects  :as scroll-handler.side-effects]
              [x.environment.scroll-handler.subs          :as scroll-handler.subs]
              [x.environment.touch-handler.subs           :as touch-handler.subs]
              [x.environment.viewport-handler.subs        :as viewport-handler.subs]
              [x.environment.window-handler.side-effects  :as window-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.environment.connection-handler.subs
(def browser-online?  connection-handler.subs/browser-online?)
(def browser-offline? connection-handler.subs/browser-offline?)

; x.environment.cookie-handler.subs
(def get-stored-cookies               cookie-handler.subs/get-stored-cookies)
(def any-cookies-stored?              cookie-handler.subs/any-cookies-stored?)
(def get-cookie-value                 cookie-handler.subs/get-cookie-value)
(def cookies-enabled-by-browser?      cookie-handler.subs/cookies-enabled-by-browser?)
(def analytics-cookies-enabled?       cookie-handler.subs/analytics-cookies-enabled?)
(def necessary-cookies-enabled?       cookie-handler.subs/necessary-cookies-enabled?)
(def user-experience-cookies-enabled? cookie-handler.subs/user-experience-cookies-enabled?)

; x.environment.css-handler.side-effects
(def add-css!    css-handler.side-effects/add-css!)
(def remove-css! css-handler.side-effects/remove-css!)

; x.environment.element-handler.side-effects
(def element-disabled?                     element-handler.side-effects/element-disabled?)
(def element-enabled?                      element-handler.side-effects/element-enabled?)
(def element-focused?                      element-handler.side-effects/element-focused?)
(def element-blurred?                      element-handler.side-effects/element-blurred?)
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
(def set-element-content!                  element-handler.side-effects/set-element-content!)
(def empty-element!                        element-handler.side-effects/empty-element!)
(def remove-element!                       element-handler.side-effects/remove-element!)
(def reveal-element!                       element-handler.side-effects/reveal-element!)
(def hide-element!                         element-handler.side-effects/hide-element!)
(def remove-element-animated!              element-handler.side-effects/remove-element-animated!)
(def hide-element-animated!                element-handler.side-effects/hide-element-animated!)
(def reveal-element-animated!              element-handler.side-effects/reveal-element-animated!)
(def mark-element-masspoint-orientation!   element-handler.side-effects/mark-element-masspoint-orientation!)
(def unmark-element-masspoint-orientation! element-handler.side-effects/unmark-element-masspoint-orientation!)
(def set-selection-start!                  element-handler.side-effects/set-selection-start!)
(def set-selection-end!                    element-handler.side-effects/set-selection-end!)
(def set-selection-range!                  element-handler.side-effects/set-selection-range!)
(def set-caret-position!                   element-handler.side-effects/set-caret-position!)
(def move-caret-to-start!                  element-handler.side-effects/move-caret-to-start!)
(def move-caret-to-end!                    element-handler.side-effects/move-caret-to-end!)

; x.environment.event-handler.side-effects
(def add-event-listener!    event-handler.side-effects/add-event-listener!)
(def remove-event-listener! event-handler.side-effects/remove-event-listener!)
(def add-event!             event-handler.side-effects/add-event!)

; x.environment.keypress-handler.events
(def set-type-mode!  keypress-handler.events/set-type-mode!)
(def quit-type-mode! keypress-handler.events/quit-type-mode!)

; x.environment.keypress-handler.subs
(def get-pressed-keys keypress-handler.subs/get-pressed-keys)
(def key-pressed?     keypress-handler.subs/key-pressed?)

; x.environment.mouse-handler.side-effects
(def prevent-selecting! mouse-handler.side-effects/prevent-selecting!)
(def enable-selecting!  mouse-handler.side-effects/enable-selecting!)

; x.environment.observe-handler.side-effects
(def setup-intersection-observer!  observe-handler.side-effects/setup-intersection-observer!)
(def remove-intersection-observer! observe-handler.side-effects/remove-intersection-observer!)

; x.environment.scroll-handler.side-effects
(def set-scroll-y!          scroll-handler.side-effects/set-scroll-y!)
(def reset-scroll-y!        scroll-handler.side-effects/reset-scroll-y!)
(def scroll-to-element-top! scroll-handler.side-effects/scroll-to-element-top!)

; x.environment.scroll-handler.subs
(def scrolled-to-top? scroll-handler.subs/scrolled-to-top?)

; x.environment.touch-handler.subs
(def touch-detected? touch-handler.subs/touch-detected?)

; x.environment.viewport-handler.subs
(def get-viewport-height      viewport-handler.subs/get-viewport-height)
(def get-viewport-width       viewport-handler.subs/get-viewport-width)
(def get-viewport-profile     viewport-handler.subs/get-viewport-profile)
(def viewport-profile-match?  viewport-handler.subs/viewport-profile-match?)
(def viewport-profiles-match? viewport-handler.subs/viewport-profiles-match?)
(def viewport-small?          viewport-handler.subs/viewport-small?)
(def viewport-medium?         viewport-handler.subs/viewport-medium?)
(def viewport-large?          viewport-handler.subs/viewport-large?)
(def get-viewport-orientation viewport-handler.subs/get-viewport-orientation)

; x.environment.window-handler.side-effects
(def set-tab-title!  window-handler.side-effects/set-tab-title!)
(def open-tab!       window-handler.side-effects/open-tab!)
(def reload-tab!     window-handler.side-effects/reload-tab!)
(def go-root!        window-handler.side-effects/go-root!)
(def go-to!          window-handler.side-effects/go-to!)
(def set-interval!   window-handler.side-effects/set-interval!)
(def clear-interval! window-handler.side-effects/clear-interval!)
(def set-timeout!    window-handler.side-effects/set-timeout!)
(def clear-timeout!  window-handler.side-effects/clear-timeout!)
