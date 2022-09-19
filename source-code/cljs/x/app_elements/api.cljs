
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.api
    (:require [x.app-elements.button.effects]
              [x.app-elements.button.side-effects]
              [x.app-elements.checkbox.events]
              [x.app-elements.checkbox.subs]
              [x.app-elements.chip-group.events]
              [x.app-elements.collect-handler.effects]
              [x.app-elements.collect-handler.subs]
              [x.app-elements.color-selector.effects]
              [x.app-elements.color-selector.events]
              [x.app-elements.combo-box.effects]
              [x.app-elements.combo-box.events]
              [x.app-elements.combo-box.side-effects]
              [x.app-elements.counter.events]
              [x.app-elements.counter.subs]
              [x.app-elements.focus-handler.side-effects]
              [x.app-elements.multi-combo-box.effects]
              [x.app-elements.multi-combo-box.events]
              [x.app-elements.multi-field.events]
              [x.app-elements.multi-field.subs]
              [x.app-elements.password-field.side-effects]
              [x.app-elements.radio-button.events]
              [x.app-elements.radio-button.subs]
              [x.app-elements.select.effects]
              [x.app-elements.select.events]
              [x.app-elements.select.subs]
              [x.app-elements.slider.effects]
              [x.app-elements.slider.events]
              [x.app-elements.slider.subs]
              [x.app-elements.switch.events]
              [x.app-elements.switch.subs]
              [x.app-elements.text-field.effects]
              [x.app-elements.text-field.events]
              [x.app-elements.text-field.side-effects]
              [x.app-elements.text-field.subs]
              [x.app-elements.anchor.views                            :as anchor.views]
              [x.app-elements.breadcrumbs.views                       :as breadcrumbs.views]
              [x.app-elements.blank.views                             :as blank.views]
              [x.app-elements.button.views                            :as button.views]
              [x.app-elements.button-separator.views                  :as button-separator.views]
              [x.app-elements.card.views                              :as card.views]
              [x.app-elements.element-components.card-group           :as element-components.card-group]
              [x.app-elements.checkbox.views                          :as checkbox.views]
              [x.app-elements.chip-group.views                        :as chip-group.views]
              [x.app-elements.chip.views                              :as chip.views]
              [x.app-elements.element-components.circle-diagram       :as element-components.circle-diagram]
              [x.app-elements.color-selector.views                    :as color-selector.views]
              [x.app-elements.color-marker.views                      :as color-marker.views]
              [x.app-elements.color-stamp.views                       :as color-stamp.views]
              [x.app-elements.element-components.column               :as element-components.column]
              [x.app-elements.combo-box.views                         :as combo-box.views]
              [x.app-elements.counter.views                           :as counter.views]
              [x.app-elements.element-components.data-table           :as element-components.data-table]
              [x.app-elements.date-field.views                        :as date-field.views]
              [x.app-elements.element-components.digit-field          :as element-components.digit-field]
              [x.app-elements.expandable.views                        :as expandable.views]
              [x.app-elements.file-drop-area.views                    :as file-drop-area.views]
              [x.app-elements.ghost.views                             :as ghost.views]
              [x.app-elements.element-components.horizontal-line      :as element-components.horizontal-line]
              [x.app-elements.element-components.horizontal-polarity  :as element-components.horizontal-polarity]
              [x.app-elements.element-components.horizontal-separator :as element-components.horizontal-separator]
              [x.app-elements.element-components.icon                 :as element-components.icon]
              [x.app-elements.element-components.icon-button          :as element-components.icon-button]
              [x.app-elements.element-components.image                :as element-components.image]
              [x.app-elements.label.views                             :as label.views]
              [x.app-elements.element-components.line-diagram         :as element-components.line-diagram]
              [x.app-elements.element-components.menu-bar             :as element-components.menu-bar]
              [x.app-elements.multiline-field.views                   :as multiline-field.views]
              [x.app-elements.multi-combo-box.views                   :as multi-combo-box.views]
              [x.app-elements.multi-field.views                       :as multi-field.views]
              [x.app-elements.element-components.overlay              :as element-components.overlay]
              [x.app-elements.password-field.views                    :as password-field.views]
              [x.app-elements.element-components.point-diagram        :as element-components.point-diagram]
              [x.app-elements.radio-button.views                      :as radio-button.views]
              [x.app-elements.element-components.row                  :as element-components.row]
              [x.app-elements.search-field.views                      :as search-field.views]
              [x.app-elements.select.views                            :as select.views]
              [x.app-elements.slider.views                            :as slider.views]
              [x.app-elements.element-components.slideshow            :as element-components.slideshow]
              [x.app-elements.switch.views                            :as switch.views]
              [x.app-elements.element-components.table                :as element-components.table]
              [x.app-elements.text.views                              :as text.views]
              [x.app-elements.text-field.views                        :as text-field.views]
              [x.app-elements.thumbnail.views                         :as thumbnail.views]
              [x.app-elements.element-components.toggle               :as element-components.toggle]
              [x.app-elements.element-components.vertical-line        :as element-components.vertical-line]
              [x.app-elements.element-components.vertical-polarity    :as element-components.vertical-polarity]
              [x.app-elements.element-components.vertical-separator   :as element-components.vertical-separator]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.element-components.*
(def anchor               anchor.views/element)
(def breadcrumbs          breadcrumbs.views/element)
(def blank                blank.views/element)
(def button               button.views/element)
(def button-separator     button-separator.views/element)
(def card                 card.views/element)
(def card-group           element-components.card-group/element)
(def checkbox             checkbox.views/element)
(def chip-group           chip-group.views/element)
(def chip                 chip.views/element)
(def circle-diagram       element-components.circle-diagram/element)
(def color-selector       color-selector.views/element)
(def color-marker         color-marker.views/element)
(def color-stamp          color-stamp.views/element)
(def column               element-components.column/element)
(def combo-box            combo-box.views/element)
(def counter              counter.views/element)
(def data-table           element-components.data-table/element)
(def date-field           date-field.views/element)
(def digit-field          element-components.digit-field/element)
(def expandable           expandable.views/element)
(def file-drop-area       file-drop-area.views/element)
(def ghost                ghost.views/element)
(def horizontal-line      element-components.horizontal-line/element)
(def horizontal-polarity  element-components.horizontal-polarity/element)
(def horizontal-separator element-components.horizontal-separator/element)
(def icon                 element-components.icon/element)
(def icon-button          element-components.icon-button/element)
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
(def row                  element-components.row/element)
(def search-field         search-field.views/element)
(def select               select.views/element)
(def slider               slider.views/element)
(def slideshow            element-components.slideshow/element)
(def switch               switch.views/element)
(def table                element-components.table/element)
(def text                 text.views/element)
(def text-field           text-field.views/element)
(def thumbnail            thumbnail.views/element)
(def toggle               element-components.toggle/element)
(def vertical-line        element-components.vertical-line/element)
(def vertical-polarity    element-components.vertical-polarity/element)
(def vertical-separator   element-components.vertical-separator/element)
