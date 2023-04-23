
(ns elements.api
    (:require [elements.button.effects]
              [elements.button.side-effects]
              [elements.checkbox.effects]
              [elements.checkbox.events]
              [elements.checkbox.subs]
              [elements.chip-group.effects]
              [elements.chip-group.events]
              [elements.color-selector.effects]
              [elements.color-selector.events]
              [elements.combo-box.effects]
              [elements.combo-box.events]
              [elements.combo-box.side-effects]
              [elements.content-swapper.side-effects]
              [elements.counter.effects]
              [elements.counter.events]
              [elements.counter.subs]
              [elements.input.events]
              [elements.input.side-effects]
              [elements.input.subs]
              [elements.multi-combo-box.effects]
              [elements.multi-combo-box.events]
              [elements.multi-combo-box.side-effects]
              [elements.multi-field.effects]
              [elements.multi-field.events]
              [elements.multi-field.subs]
              [elements.password-field.side-effects]
              [elements.plain-field.effects]
              [elements.plain-field.events]
              [elements.plain-field.side-effects]
              [elements.radio-button.effects]
              [elements.radio-button.events]
              [elements.radio-button.subs]
              [elements.select.effects]
              [elements.select.events]
              [elements.select.side-effects]
              [elements.select.subs]
              [elements.slider.effects]
              [elements.slider.events]
              [elements.slider.subs]
              [elements.switch.effects]
              [elements.switch.subs]
              [elements.text-field.effects]
              [elements.text-field.side-effects]
              [elements.text-field.subs]
              [elements.breadcrumbs.views          :as breadcrumbs.views]
              [elements.blank.views                :as blank.views]
              [elements.button.views               :as button.views]
              [elements.card.views                 :as card.views]
              [elements.checkbox.views             :as checkbox.views]
              [elements.chip-group.views           :as chip-group.views]
              [elements.chip.views                 :as chip.views]
              [elements.circle-diagram.views       :as circle-diagram.views]
              [elements.color-selector.views       :as color-selector.views]
              [elements.column.views               :as column.views]
              [elements.combo-box.views            :as combo-box.views]
              [elements.content-swapper.views      :as content-swapper.views]
              [elements.counter.views              :as counter.views]
              [elements.date-field.views           :as date-field.views]
              [elements.digit-field.views          :as digit-field.views]
              [elements.dropdown-menu.views        :as dropdown-menu.views]
              [elements.element.views              :as element.views]
              [elements.expandable.views           :as expandable.views]
              [elements.ghost.views                :as ghost.views]
              [elements.horizontal-group.views     :as horizontal-group.views]
              [elements.horizontal-line.views      :as horizontal-line.views]
              [elements.horizontal-polarity.views  :as horizontal-polarity.views]
              [elements.horizontal-separator.views :as horizontal-separator.views]
              [elements.icon.views                 :as icon.views]
              [elements.icon-button.views          :as icon-button.views]
              [elements.image.views                :as image.views]
              [elements.label.views                :as label.views]
              [elements.line-diagram.views         :as line-diagram.views]
              [elements.menu-bar.views             :as menu-bar.views]
              [elements.multiline-field.views      :as multiline-field.views]
              [elements.multi-combo-box.views      :as multi-combo-box.views]
              [elements.multi-field.views          :as multi-field.views]
              [elements.notification-bubble.views  :as notification-bubble.views]
              [elements.password-field.views       :as password-field.views]
              [elements.plain-field.views          :as plain-field.views]
              [elements.point-diagram.views        :as point-diagram.views]
              [elements.radio-button.views         :as radio-button.views]
              [elements.row.views                  :as row.views]
              [elements.search-field.views         :as search-field.views]
              [elements.select.views               :as select.views]
              [elements.slider.views               :as slider.views]
              [elements.stepper.views              :as stepper.views]
              [elements.switch.views               :as switch.views]
              [elements.text.views                 :as text.views]
              [elements.text-field.views           :as text-field.views]
              [elements.thumbnail.views            :as thumbnail.views]
              [elements.toggle.views               :as toggle.views]
              [elements.vertical-group.views       :as vertical-group.views]
              [elements.vertical-line.views        :as vertical-line.views]
              [elements.vertical-polarity.views    :as vertical-polarity.views]
              [elements.vertical-separator.views   :as vertical-separator.views]
              [window-observer.api                 :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Initializing the viewport resize observer before using subscriptions for viewport dimensions
(window-observer/listen-to-viewport-resize!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; elements.*.views
(def breadcrumbs          breadcrumbs.views/element)
(def blank                blank.views/element)
(def button               button.views/element)
(def card                 card.views/element)
(def checkbox             checkbox.views/element)
(def chip-group           chip-group.views/element)
(def chip                 chip.views/element)
(def circle-diagram       circle-diagram.views/element)
(def color-selector       color-selector.views/element)
(def column               column.views/element)
(def combo-box            combo-box.views/element)
(def content-swapper      content-swapper.views/element)
(def counter              counter.views/element)
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
(def icon                 icon.views/element)
(def icon-button          icon-button.views/element)
(def image                image.views/element)
(def label                label.views/element)
(def line-diagram         line-diagram.views/element)
(def menu-bar             menu-bar.views/element)
(def multi-combo-box      multi-combo-box.views/element)
(def multi-field          multi-field.views/element)
(def multiline-field      multiline-field.views/element)
(def notification-bubble  notification-bubble.views/element)
(def password-field       password-field.views/element)
(def plain-field          plain-field.views/element)
(def point-diagram        point-diagram.views/element)
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
(def vertical-separator   vertical-separator.views/element)
