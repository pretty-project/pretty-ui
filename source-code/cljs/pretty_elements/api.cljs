
(ns pretty-elements.api
    (:require [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.adornment-group.views      :as adornment-group.views]
              [pretty-elements.blank.views                :as blank.views]
              [pretty-elements.breadcrumbs.views          :as breadcrumbs.views]
              [pretty-elements.button.views               :as button.views]
              [pretty-elements.card.views                 :as card.views]
              [pretty-elements.chip.views                 :as chip.views]
              [pretty-elements.column.views               :as column.views]
              [pretty-elements.content-swapper.views      :as content-swapper.views]
              [pretty-elements.data-table.views           :as data-table.views]
              [pretty-elements.dropdown-menu.views        :as dropdown-menu.views]
              [pretty-elements.expandable.views           :as expandable.views]
              [pretty-elements.ghost.views                :as ghost.views]
              [pretty-elements.horizontal-group.views     :as horizontal-group.views]
              [pretty-elements.horizontal-line.views      :as horizontal-line.views]
              [pretty-elements.horizontal-separator.views :as horizontal-separator.views]
              [pretty-elements.horizontal-spacer.views    :as horizontal-spacer.views]
              [pretty-elements.icon-button.views          :as icon-button.views]
              [pretty-elements.icon.views                 :as icon.views]
              [pretty-elements.image.views                :as image.views]
              [pretty-elements.label.views                :as label.views]
              [pretty-elements.menu-bar.views             :as menu-bar.views]
              [pretty-elements.notification-bubble.views  :as notification-bubble.views]
              [pretty-elements.row.views                  :as row.views]
              [pretty-elements.stepper.views              :as stepper.views]
              [pretty-elements.text.views                 :as text.views]
              [pretty-elements.thumbnail.views            :as thumbnail.views]
              [pretty-elements.toggle.views               :as toggle.views]
              [pretty-elements.vertical-group.views       :as vertical-group.views]
              [pretty-elements.vertical-line.views        :as vertical-line.views]
              [pretty-elements.vertical-spacer.views      :as vertical-spacer.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/element)
(def adornment            adornment.views/element)
(def adornment-group      adornment-group.views/element)
(def breadcrumbs          breadcrumbs.views/element)
(def blank                blank.views/element)
(def button               button.views/element)
(def card                 card.views/element)
(def chip                 chip.views/element)
(def column               column.views/element)
(def content-swapper      content-swapper.views/element)
(def data-table           data-table.views/element)
(def dropdown-menu        dropdown-menu.views/element)
(def expandable           expandable.views/element)
(def ghost                ghost.views/element)
(def horizontal-group     horizontal-group.views/element)
(def horizontal-line      horizontal-line.views/element)
(def horizontal-separator horizontal-separator.views/element)
(def horizontal-spacer    horizontal-spacer.views/element)
(def icon                 icon.views/element)
(def icon-button          icon-button.views/element)
(def image                image.views/element)
(def label                label.views/element)
(def menu-bar             menu-bar.views/element)
(def notification-bubble  notification-bubble.views/element)
(def row                  row.views/element)
(def stepper              stepper.views/element)
(def text                 text.views/element)
(def thumbnail            thumbnail.views/element)
(def toggle               toggle.views/element)
(def vertical-group       vertical-group.views/element)
(def vertical-line        vertical-line.views/element)
(def vertical-spacer      vertical-spacer.views/element)
