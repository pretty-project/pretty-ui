
(ns website.api
    (:require [website.language-selector.effects]
              [website.sidebar.effects]
              [website.sidebar.subs]
              [website.contacts.views           :as contacts.views]
              [website.copyright-label.views    :as copyright-label.views]
              [website.created-by-link.views    :as created-by-link.views]
              [website.credits.views            :as credits.views]
              [website.follow-us.views          :as follow-us.views]
              [website.language-selector.views  :as language-selector.views]
              [website.mt-logo.views            :as mt-logo.views]
              [website.menu.views               :as menu.views]
              [website.navbar.views             :as navbar.views]
              [website.navbar-menu.views        :as navbar-menu.views]
              [website.sidebar.views            :as sidebar.views]
              [website.social-media-links.views :as social-media-links.views]
              [website.scroll-icon.views        :as scroll-icon.views]
              [website.scroll-sensor.views      :as scroll-sensor.views]
              [website.scroll-to-top.views      :as scroll-to-top.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; website.*.views
(def contacts          contacts.views/component)
(def copyright-label   copyright-label.views/component)
(def created-by-link   created-by-link.views/component)
(def credits           credits.views/component)
(def follow-us         follow-us.views/component)
(def language-selector language-selector.views/component)
(def mt-logo           mt-logo.views/component)
(def menu              menu.views/component)
(def navbar            navbar.views/component)
(def navbar-menu       navbar-menu.views/component)
(def sidebar           sidebar.views/component)
(def scroll-icon       scroll-icon.views/component)
(def scroll-sensor     scroll-sensor.views/component)
(def scroll-to-top     scroll-to-top.views/component)
