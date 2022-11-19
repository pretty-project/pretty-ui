
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.api
    (:require [elements.button.effects]
              [elements.button.side-effects]
              [elements.checkbox.events]
              [elements.checkbox.subs]
              [elements.chip-group.events]
              [elements.color-selector.effects]
              [elements.color-selector.events]
              [elements.combo-box.effects]
              [elements.combo-box.events]
              [elements.combo-box.side-effects]
              [elements.counter.events]
              [elements.counter.subs]
              [elements.element.side-effects]
              [elements.multi-combo-box.effects]
              [elements.multi-combo-box.events]
              [elements.multi-field.effects]
              [elements.multi-field.events]
              [elements.multi-field.subs]
              [elements.password-field.side-effects]
              [elements.radio-button.events]
              [elements.radio-button.subs]
              [elements.select.effects]
              [elements.select.events]
              [elements.select.subs]
              [elements.slider.effects]
              [elements.slider.events]
              [elements.slider.subs]
              [elements.switch.events]
              [elements.switch.subs]
              [elements.text-field.effects]
              [elements.text-field.events]
              [elements.text-field.side-effects]
              [elements.text-field.subs]
              [elements.anchor.views                            :as anchor.views]
              [elements.breadcrumbs.views                       :as breadcrumbs.views]
              [elements.blank.views                             :as blank.views]
              [elements.button.views                            :as button.views]
              [elements.button-separator.views                  :as button-separator.views]
              [elements.card.views                              :as card.views]
              [elements.checkbox.views                          :as checkbox.views]
              [elements.chip-group.views                        :as chip-group.views]
              [elements.chip.views                              :as chip.views]
              [elements.circle-diagram.views                    :as circle-diagram.views]
              [elements.color-selector.views                    :as color-selector.views]
              [elements.color-marker.views                      :as color-marker.views]
              [elements.color-stamp.views                       :as color-stamp.views]
              [elements.column.views                            :as column.views]
              [elements.combo-box.views                         :as combo-box.views]
              [elements.counter.views                           :as counter.views]
              [elements.date-field.views                        :as date-field.views]
              [elements.digit-field.views                       :as digit-field.views]
              [elements.expandable.views                        :as expandable.views]
              [elements.file-drop-area.views                    :as file-drop-area.views]
              [elements.ghost.views                             :as ghost.views]
              [elements.horizontal-line.views                   :as horizontal-line.views]
              [elements.horizontal-polarity.views               :as horizontal-polarity.views]
              [elements.element-components.horizontal-separator :as element-components.horizontal-separator]
              [elements.icon.views                              :as icon.views]
              [elements.icon-button.views                       :as icon-button.views]
              [elements.element-components.image                :as element-components.image]
              [elements.label.views                             :as label.views]
              [elements.element-components.line-diagram         :as element-components.line-diagram]
              [elements.element-components.menu-bar             :as element-components.menu-bar]
              [elements.multiline-field.views                   :as multiline-field.views]
              [elements.multi-combo-box.views                   :as multi-combo-box.views]
              [elements.multi-field.views                       :as multi-field.views]
              [elements.element-components.overlay              :as element-components.overlay]
              [elements.password-field.views                    :as password-field.views]
              [elements.element-components.point-diagram        :as element-components.point-diagram]
              [elements.radio-button.views                      :as radio-button.views]
              [elements.row.views                               :as row.views]
              [elements.search-field.views                      :as search-field.views]
              [elements.select.views                            :as select.views]
              [elements.slider.views                            :as slider.views]
              [elements.switch.views                            :as switch.views]
              [elements.text.views                              :as text.views]
              [elements.text-field.views                        :as text-field.views]
              [elements.thumbnail.views                         :as thumbnail.views]
              [elements.element-components.toggle               :as element-components.toggle]
              [elements.element-components.vertical-line        :as element-components.vertical-line]
              [elements.vertical-polarity.views                 :as vertical-polarity.views]
              [elements.element-components.vertical-separator   :as element-components.vertical-separator]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; elements.*.views
(def anchor               anchor.views/element)
(def breadcrumbs          breadcrumbs.views/element)
(def blank                blank.views/element)
(def button               button.views/element)
(def button-separator     button-separator.views/element)
(def card                 card.views/element)
(def checkbox             checkbox.views/element)
(def chip-group           chip-group.views/element)
(def chip                 chip.views/element)
(def circle-diagram       circle-diagram.views/element)
(def color-selector       color-selector.views/element)
(def color-marker         color-marker.views/element)
(def color-stamp          color-stamp.views/element)
(def column               column.views/element)
(def combo-box            combo-box.views/element)
(def counter              counter.views/element)
(def date-field           date-field.views/element)
(def digit-field          digit-field.views/element)
(def expandable           expandable.views/element)
(def file-drop-area       file-drop-area.views/element)
(def ghost                ghost.views/element)
(def horizontal-line      horizontal-line.views/element)
(def horizontal-polarity  horizontal-polarity.views/element)
(def horizontal-separator element-components.horizontal-separator/element)
(def icon                 icon.views/element)
(def icon-button          icon-button.views/element)
(def image                element-components.image/element)
(def label                label.views/element)
(def line-diagram         element-components.line-diagram/element)
(def menu-bar             element-components.menu-bar/element)
(def multi-combo-box      multi-combo-box.views/element)
(def multi-field          multi-field.views/element)
(def multiline-field      multiline-field.views/element)
(def overlay              element-components.overlay/element)
(def password-field       password-field.views/element)
(def point-diagram        element-components.point-diagram/element)
(def radio-button         radio-button.views/element)
(def row                  row.views/element)
(def search-field         search-field.views/element)
(def select               select.views/element)
(def slider               slider.views/element)
(def switch               switch.views/element)
(def text                 text.views/element)
(def text-field           text-field.views/element)
(def thumbnail            thumbnail.views/element)
(def toggle               element-components.toggle/element)
(def vertical-line        element-components.vertical-line/element)
(def vertical-polarity    vertical-polarity.views/element)
(def vertical-separator   element-components.vertical-separator/element)
