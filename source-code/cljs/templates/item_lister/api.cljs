
(ns templates.item-lister.api
    (:require [engines.item-lister.api]
              [templates.item-lister.core.subs]
              [templates.item-lister.update.effects]
              [templates.item-lister.body.views    :as body.views]
              [templates.item-lister.footer.views  :as footer.views]
              [templates.item-lister.header.views  :as header.views]
              [templates.item-lister.menu.views    :as menu.views]
              [templates.item-lister.wrapper.views :as wrapper.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.item-lister.body.views
(def body body.views/body)

; templates.item-lister.footer.views
(def footer footer.views/footer)

; templates.item-lister.header.views
(def compact-bar header.views/compact-bar)
(def label-bar   header.views/label-bar)
(def list-header header.views/list-header)
(def search-bar  header.views/search-bar)

; WARNING! DEPRECATED! DO NOT USE!
(def header      header.views/header)
(def search-field       header.views/search-field)
(def search-description header.views/search-description)
; WARNING! DEPRECATED! DO NOT USE!


; templates.item-lister.menu.views
(def create-item-button menu.views/create-item-button)
(def menu               menu.views/menu)

; templates.item-lister.wrapper.views
(def compact-wrapper wrapper.views/compact-wrapper)
(def wide-wrapper    wrapper.views/wide-wrapper)
