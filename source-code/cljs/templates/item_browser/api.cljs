
(ns templates.item-browser.api
    (:require [engines.item-browser.api]
              [templates.item-browser.core.effects]
              [templates.item-browser.core.subs]
              [templates.item-browser.update.effects]
              [templates.item-browser.body.views   :as body.views]
              [templates.item-browser.footer.views :as footer.views]
              [templates.item-browser.header.views :as header.views]
              [templates.item-browser.menu.views   :as menu.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.item-browser.body.views
(def body body.views/body)

; templates.item-browser.footer.views
(def footer footer.views/footer)

; templates.item-browser.header.views
(def header             header.views/header)
(def search-field       header.views/search-field)
(def search-description header.views/search-description)
(def item-info          header.views/item-info)
(def compact-bar   header.views/compact-bar)

; templates.item-browser.menu.views
(def menu menu.views/menu)
