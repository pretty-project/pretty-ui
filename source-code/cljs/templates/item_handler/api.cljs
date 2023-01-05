
(ns templates.item-handler.api
    (:require [engines.item-handler.api]
              [templates.item-handler.core.subs]
              [templates.item-handler.update.effects]
              [templates.item-handler.body.views    :as body.views]
              [templates.item-handler.footer.views  :as footer.views]
              [templates.item-handler.header.views  :as header.views]
              [templates.item-handler.menu.views    :as menu.views]
              [templates.item-handler.wrapper.views :as wrapper.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.item-handler.body.views
(def body body.views/body)

; templates.item-handler.footer.views
(def footer footer.views/footer)

; templates.item-handler.header.views
(def menu-bar    header.views/menu-bar)
(def control-bar header.views/control-bar)
(def label-bar   header.views/label-bar)

; WARNING! DEPRECATED! DO NOT USE!
(def header header.views/header)
; WARNING! DEPRECATED! DO NOT USE!

; templates.item-handler.menu.views
(def menu menu.views/menu)

; templates.item-handler.wrapper.views
(def wrapper wrapper.views/wrapper)
