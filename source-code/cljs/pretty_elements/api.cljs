
(ns pretty-elements.api
    (:require [pretty-elements.button.effects]
              [pretty-elements.button.side-effects]
              [pretty-elements.checkbox.effects]
              [pretty-elements.checkbox.events]
              [pretty-elements.checkbox.subs]
              [pretty-elements.chip-group.effects]
              [pretty-elements.chip-group.events]
              [pretty-elements.color-selector.effects]
              [pretty-elements.color-selector.events]
              [pretty-elements.combo-box.effects]
              [pretty-elements.combo-box.events]
              [pretty-elements.combo-box.side-effects]
              [pretty-elements.content-swapper.effects]
              [pretty-elements.content-swapper.side-effects]
              [pretty-elements.counter.effects]
              [pretty-elements.counter.events]
              [pretty-elements.counter.subs]
              [pretty-elements.form.effects]
              [pretty-elements.form.side-effects]
              [pretty-elements.input.events]
              [pretty-elements.input.side-effects]
              [pretty-elements.input.subs]
              [pretty-elements.multi-combo-box.effects]
              [pretty-elements.multi-combo-box.events]
              [pretty-elements.multi-combo-box.side-effects]
              [pretty-elements.multi-field.effects]
              [pretty-elements.multi-field.events]
              [pretty-elements.multi-field.subs]
              [pretty-elements.password-field.effects]
              [pretty-elements.password-field.side-effects]
              [pretty-elements.plain-field.effects]
              [pretty-elements.plain-field.events]
              [pretty-elements.plain-field.side-effects]
              [pretty-elements.radio-button.effects]
              [pretty-elements.radio-button.events]
              [pretty-elements.radio-button.subs]
              [pretty-elements.select.effects]
              [pretty-elements.select.events]
              [pretty-elements.select.side-effects]
              [pretty-elements.select.subs]
              [pretty-elements.slider.effects]
              [pretty-elements.slider.events]
              [pretty-elements.slider.subs]
              [pretty-elements.switch.effects]
              [pretty-elements.switch.subs]
              [pretty-elements.text-field.effects]
              [pretty-elements.text-field.side-effects]
              [pretty-elements.blank.views                :as blank.views]
              [pretty-elements.breadcrumbs.views          :as breadcrumbs.views]
              [pretty-elements.button.views               :as button.views]
              [pretty-elements.card.views                 :as card.views]
              [pretty-elements.checkbox.views             :as checkbox.views]
              [pretty-elements.chip-group.views           :as chip-group.views]
              [pretty-elements.chip.views                 :as chip.views]
              [pretty-elements.color-selector.views       :as color-selector.views]
              [pretty-elements.column.views               :as column.views]
              [pretty-elements.combo-box.views            :as combo-box.views]
              [pretty-elements.content-swapper.views      :as content-swapper.views]
              [pretty-elements.counter.views              :as counter.views]
              [pretty-elements.data-table.views           :as data-table.views]
              [pretty-elements.date-field.views           :as date-field.views]
              [pretty-elements.digit-field.views          :as digit-field.views]
              [pretty-elements.dropdown-menu.views        :as dropdown-menu.views]
              [pretty-elements.element.views              :as element.views]
              [pretty-elements.expandable.views           :as expandable.views]
              [pretty-elements.ghost.views                :as ghost.views]
              [pretty-elements.horizontal-group.views     :as horizontal-group.views]
              [pretty-elements.horizontal-line.views      :as horizontal-line.views]
              [pretty-elements.horizontal-polarity.views  :as horizontal-polarity.views]
              [pretty-elements.horizontal-separator.views :as horizontal-separator.views]
              [pretty-elements.horizontal-spacer.views    :as horizontal-spacer.views]
              [pretty-elements.icon-button.views          :as icon-button.views]
              [pretty-elements.icon.views                 :as icon.views]
              [pretty-elements.image.views                :as image.views]
              [pretty-elements.label.views                :as label.views]
              [pretty-elements.menu-bar.views             :as menu-bar.views]
              [pretty-elements.multi-combo-box.views      :as multi-combo-box.views]
              [pretty-elements.multi-field.views          :as multi-field.views]
              [pretty-elements.multiline-field.views      :as multiline-field.views]
              [pretty-elements.notification-bubble.views  :as notification-bubble.views]
              [pretty-elements.number-field.views         :as number-field.views]
              [pretty-elements.password-field.views       :as password-field.views]
              [pretty-elements.plain-field.views          :as plain-field.views]
              [pretty-elements.radio-button.views         :as radio-button.views]
              [pretty-elements.row.views                  :as row.views]
              [pretty-elements.search-field.views         :as search-field.views]
              [pretty-elements.select.views               :as select.views]
              [pretty-elements.slider.views               :as slider.views]
              [pretty-elements.stepper.views              :as stepper.views]
              [pretty-elements.switch.views               :as switch.views]
              [pretty-elements.text-field.views           :as text-field.views]
              [pretty-elements.text.views                 :as text.views]
              [pretty-elements.thumbnail.views            :as thumbnail.views]
              [pretty-elements.toggle.views               :as toggle.views]
              [pretty-elements.vertical-group.views       :as vertical-group.views]
              [pretty-elements.vertical-line.views        :as vertical-line.views]
              [pretty-elements.vertical-polarity.views    :as vertical-polarity.views]
              [pretty-elements.vertical-spacer.views      :as vertical-spacer.views]
              [window-observer.api                        :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Initializing the viewport resize observer before using subscriptions for viewport dimensions
(window-observer/listen-to-viewport-resize!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.*)
(def breadcrumbs          breadcrumbs.views/element)
(def blank                blank.views/element)
(def button               button.views/element)
(def card                 card.views/element)
(def checkbox             checkbox.views/element)
(def chip-group           chip-group.views/element)
(def chip                 chip.views/element)
(def color-selector       color-selector.views/element)
(def column               column.views/element)
(def combo-box            combo-box.views/element)
(def content-swapper      content-swapper.views/element)
(def counter              counter.views/element)
(def data-table           data-table.views/element)
(def date-field           date-field.views/element)
(def digit-field          digit-field.views/element)
(def dropdown-menu        dropdown-menu.views/element)
(def element-label        element.views/element-label)
(def expandable           expandable.views/element)
(def ghost                ghost.views/element)
(def horizontal-group     horizontal-group.views/element)
(def horizontal-line      horizontal-line.views/element)
(def horizontal-polarity  horizontal-polarity.views/element)
(def horizontal-separator horizontal-separator.views/element)
(def horizontal-spacer    horizontal-spacer.views/element)
(def icon                 icon.views/element)
(def icon-button          icon-button.views/element)
(def image                image.views/element)
(def label                label.views/element)
(def menu-bar             menu-bar.views/element)
(def multi-combo-box      multi-combo-box.views/element)
(def multi-field          multi-field.views/element)
(def multiline-field      multiline-field.views/element)
(def notification-bubble  notification-bubble.views/element)
(def number-field         number-field.views/element)
(def password-field       password-field.views/element)
(def plain-field          plain-field.views/element)
(def radio-button         radio-button.views/element)
(def row                  row.views/element)
(def search-field         search-field.views/element)
(def select               select.views/element)
(def slider               slider.views/element)
(def stepper              stepper.views/element)
(def switch               switch.views/element)
(def text                 text.views/element)
(def text-field           text-field.views/element)
(def thumbnail            thumbnail.views/element)
(def toggle               toggle.views/element)
(def vertical-group       vertical-group.views/element)
(def vertical-line        vertical-line.views/element)
(def vertical-polarity    vertical-polarity.views/element)
(def vertical-spacer      vertical-spacer.views/element)
