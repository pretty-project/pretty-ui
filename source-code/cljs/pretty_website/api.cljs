
(ns pretty-website.api
    (:require [pretty-website.sidebar.effects]
              [pretty-website.sidebar.side-effects]
              [pretty-website.contacts.views          :as contacts.views]
              [pretty-website.follow-us-links.views   :as follow-us-links.views]
              [pretty-website.language-selector.views :as language-selector.views]
              [pretty-website.multi-menu.views        :as multi-menu.views]
              [pretty-website.scroll-icon.views       :as scroll-icon.views]
              [pretty-website.scroll-sensor.views     :as scroll-sensor.views]
              [pretty-website.sidebar.views           :as sidebar.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial First steps
; @redirect (tutorials/first-steps)

; @tutorial Introduction
; @redirect (tutorials/introduction)

; @tutorial Parametering
; @redirect (tutorials/parametering)

; @tutorial Presets
; @redirect (tutorials/presets)

; @tutorial Event handlers
; @redirect (tutorials/event-handlers)

; @tutorial Content types
; @redirect (tutorials/content-types)

; @tutorial Value paths of inputs
; @redirect (tutorials/value-paths-of-inputs)

; @tutorial Option paths of optionable inputs
; @redirect (tutorials/option-paths-of-optionable-inputs)

; @tutorial Customizing items
; @redirect (tutorials/customizing-items)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/component)
(def contacts          contacts.views/component)
(def follow-us-links   follow-us-links.views/component)
(def language-selector language-selector.views/component)
(def multi-menu        multi-menu.views/component)
(def sidebar           sidebar.views/component)
(def scroll-icon       scroll-icon.views/component)
(def scroll-sensor     scroll-sensor.views/component)
