
(ns templates.item-selector.api
    (:require [templates.item-selector.body.effects]
              [templates.item-selector.body.events]
              [templates.item-selector.body.subs]
              [templates.item-selector.core.effects]
              [templates.item-selector.body.views   :as body.views]
              [templates.item-selector.footer.views :as footer.views]
              [templates.item-selector.header.views :as header.views]
              [templates.item-selector.item.views   :as item.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; templates.item-selector.body.views
(def body body.views/body)

; templates.item-selector.footer.views
(def footer footer.views/footer)

; templates.item-selector.header.views
(def control-bar header.views/control-bar)
(def label-bar   header.views/label-bar)

; templates.item-selector.item.views
(def item-counter item.views/item-counter)
(def item-marker  item.views/item-marker)
