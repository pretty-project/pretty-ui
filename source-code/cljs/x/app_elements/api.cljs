
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
              [x.app-elements.collect-handler.effects]
              [x.app-elements.collect-handler.events]
              [x.app-elements.collect-handler.subs]
              [x.app-elements.color-selector.effects]
              [x.app-elements.color-selector.events]
              [x.app-elements.combo-box.effects]
              [x.app-elements.combo-box.events]
              [x.app-elements.combo-box.side-effects]
              [x.app-elements.content-handler.events]
              [x.app-elements.content-handler.subs]
              [x.app-elements.expand-handler.events]
              [x.app-elements.expand-handler.subs]
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
              [x.app-elements.surface-handler.events]
              [x.app-elements.surface-handler.subs]
              [x.app-elements.switch.events]
              [x.app-elements.switch.subs]
              [x.app-elements.submit-button.subs]
              [x.app-elements.text-field.effects]
              [x.app-elements.text-field.events]
              [x.app-elements.text-field.side-effects]
              [x.app-elements.text-field.subs]
              [x.app-elements.anchor.views                            :as anchor.views]
              [x.app-elements.button.views                            :as button.views]
              [x.app-elements.button-separator.views                  :as button-separator.views]
              [x.app-elements.element-components.card                 :as element-components.card]
              [x.app-elements.element-components.card-group           :as element-components.card-group]
              [x.app-elements.checkbox.views                          :as checkbox.views]
              [x.app-elements.chip-group.views                        :as chip-group.views]
              [x.app-elements.element-components.chip                 :as element-components.chip]
              [x.app-elements.element-components.circle-diagram       :as element-components.circle-diagram]
              [x.app-elements.color-selector.views                    :as color-selector.views]
              [x.app-elements.color-marker.views                      :as color-marker.views]
              [x.app-elements.color-stamp.views                       :as color-stamp.views]
              [x.app-elements.element-components.column               :as element-components.column]
              [x.app-elements.combo-box.views                         :as combo-box.views]
              [x.app-elements.element-components.counter              :as element-components.counter]
              [x.app-elements.element-components.data-table           :as element-components.data-table]
              [x.app-elements.date-field.views                        :as date-field.views]
              [x.app-elements.element-components.digit-field          :as element-components.digit-field]
              [x.app-elements.element-components.expandable           :as element-components.expandable]
              [x.app-elements.element-components.file-drop-area       :as element-components.file-drop-area]
              [x.app-elements.ghost.views                             :as ghost.views]
              [x.app-elements.element-components.horizontal-line      :as element-components.horizontal-line]
              [x.app-elements.element-components.horizontal-polarity  :as element-components.horizontal-polarity]
              [x.app-elements.element-components.horizontal-separator :as element-components.horizontal-separator]
              [x.app-elements.element-components.icon                 :as element-components.icon]
              [x.app-elements.element-components.icon-button          :as element-components.icon-button]
              [x.app-elements.element-components.image                :as element-components.image]
              [x.app-elements.element-components.info-text            :as element-components.info-text]
              [x.app-elements.element-components.label                :as element-components.label]
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
              [x.app-elements.element-components.slideshow            :as element-components.slideshow]
              [x.app-elements.submit-button.views                     :as submit-button.views]
              [x.app-elements.switch.views                            :as switch.views]
              [x.app-elements.element-components.table                :as element-components.table]
              [x.app-elements.element-components.text                 :as element-components.text]
              [x.app-elements.text-field.views                        :as text-field.views]
              [x.app-elements.thumbnail.views                         :as thumbnail.views]
              [x.app-elements.element-components.toggle               :as element-components.toggle]
              [x.app-elements.element-components.vertical-line        :as element-components.vertical-line]
              [x.app-elements.element-components.vertical-polarity    :as element-components.vertical-polarity]
              [x.app-elements.element-components.vertical-separator   :as element-components.vertical-separator]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name optional element-id
;  Az egyes elemeket lehetséges element-id azonosító nélkül használni.
;  Azonban, ha az elem újra rendererelődik (Pl.: megváltoztatod az element-props térképet),
;  akkor az elem új azonosítót kap és az elem konstansként megjelelölt
;  tulajdonságai elérhetetlenné válnak az elem számára.
;
; @name constant
;  - Pl.: {:value-path (vector)(constant)(opt)}
;    Az elemek azon tulajdonságai konstansak, amelyek a Re-Frame adatbázisba kerülnek az elem
;    React-fába való csatolásakor, ezért azokat a paraméterezés megváltoztatásával felülírni
;    nem lehetséges.
;  - XXX#8099 Az x.app-components.stated/component komponens nem képes :component-did-update
;             életciklusban az elem megváltozott paramétereit a Re-Frame adatbázisban felülírni!
;
; @name default-value
;  Az egyes input elemek értéke a {:default-value ...} tulajdonságukkal kerül
;  behelyettesítésre amíg a {:value-path ...} Re-Frame adatbázis útvonalon
;  található érték nil. A default-value értéke NEM íródik a {:value-path ...}
;  adatbázis útvonlra.
;
; @name initial-value
;  Az egyes input elemek React-fába csatolásakor az {:initial-value ...}
;  tulajdonságának értéke az {:value-path ...} Re-Frame adatbázis útvonalra íródik.
;
; @name initial-options
;  TODO ...
;
; @name element-height
;  Egységes element magasságok:
;  XXS:  6px
;  XS:  12px
;  S:   18px
;  M:   24px
;  L:   30px
;  XL:  36px
;  XXL: 48px



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  Amikor egy elem paraméterei kívülről megváltoznak, akkor az elemen lévő fókusz,
;  szövegkijelölés és/vagy kurzor-pozíció a paraméterváltozás miatti újrarenderelődés
;  hatására elveszik.
;  Ha ez problémát jelent, akkor célszerű a paramétert Re-Frame eseménnyel
;  változtatni, ami nem okozza az elem konténerének újrarenderelődését.
;
;  [:elements/set-element-prop! ...]



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.element-components.*
(def anchor               anchor.views/element)
(def button               button.views/element)
(def button-separator     button-separator.views/element)
(def card                 element-components.card/element)
(def card-group           element-components.card-group/element)
(def checkbox             checkbox.views/element)
(def chip-group           chip-group.views/element)
(def chip                 element-components.chip/element)
(def circle-diagram       element-components.circle-diagram/element)
(def color-selector       color-selector.views/element)
(def color-marker         color-marker.views/element)
(def color-stamp          color-stamp.views/element)
(def column               element-components.column/element)
(def combo-box            combo-box.views/element)
(def counter              element-components.counter/element)
(def data-table           element-components.data-table/element)
(def date-field           date-field.views/element)
(def digit-field          element-components.digit-field/element)
(def expandable           element-components.expandable/element)
(def file-drop-area       element-components.file-drop-area/element)
(def ghost                ghost.views/element)
(def horizontal-line      element-components.horizontal-line/element)
(def horizontal-polarity  element-components.horizontal-polarity/element)
(def horizontal-separator element-components.horizontal-separator/element)
(def icon                 element-components.icon/element)
(def icon-button          element-components.icon-button/element)
(def image                element-components.image/element)
(def info-text            element-components.info-text/element)
(def label                element-components.label/element)
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
(def slideshow            element-components.slideshow/element)
(def submit-button        submit-button.views/element)
(def switch               switch.views/element)
(def table                element-components.table/element)
(def text                 element-components.text/element)
(def text-field           text-field.views/element)
(def thumbnail            thumbnail.views/element)
(def toggle               element-components.toggle/element)
(def vertical-line        element-components.vertical-line/element)
(def vertical-polarity    element-components.vertical-polarity/element)
(def vertical-separator   element-components.vertical-separator/element)
