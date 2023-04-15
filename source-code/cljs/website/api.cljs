
(ns website.api
    (:require [website.sidebar.side-effects]
              [website.contacts.views           :as contacts.views]
              [website.follow-us.views          :as follow-us.views]
              [website.language-selector.views  :as language-selector.views]
              [website.multi-menu.views         :as multi-menu.views]
              [website.sidebar.views            :as sidebar.views]
              [website.scroll-icon.views        :as scroll-icon.views]
              [website.scroll-sensor.views      :as scroll-sensor.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; website.*.views
(def contacts          contacts.views/component)
(def follow-us         follow-us.views/component)
(def language-selector language-selector.views/component)
(def multi-menu        multi-menu.views/component)
(def sidebar           sidebar.views/component)
(def scroll-icon       scroll-icon.views/component)
(def scroll-sensor     scroll-sensor.views/component)
