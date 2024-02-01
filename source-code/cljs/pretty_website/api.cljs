
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

; @redirect (*/view)
(def contacts          contacts.views/view)
(def follow-us-links   follow-us-links.views/view)
(def language-selector language-selector.views/view)
(def multi-menu        multi-menu.views/view)
(def sidebar           sidebar.views/view)
(def scroll-icon       scroll-icon.views/view)
(def scroll-sensor     scroll-sensor.views/view)
