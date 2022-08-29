
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
              [x.app-elements.collect-handler.effects]
              [x.app-elements.collect-handler.events]
              [x.app-elements.collect-handler.subs]
              [x.app-elements.color-selector.effects]
              [x.app-elements.color-selector.events]
              [x.app-elements.content-handler.events]
              [x.app-elements.content-handler.subs]
              [x.app-elements.expand-handler.events]
              [x.app-elements.expand-handler.subs]
              [x.app-elements.focus-handler.side-effects]
              [x.app-elements.surface-handler.events]
              [x.app-elements.surface-handler.subs]
              [x.app-elements.passfield-handler.events]
              [x.app-elements.passfield-handler.subs]
              [x.app-elements.select.effects]
              [x.app-elements.select.events]
              [x.app-elements.anchor.views                            :as anchor.views]
              [x.app-elements.button.views                            :as button.views]
              [x.app-elements.button-separator.views                  :as button-separator.views]
              [x.app-elements.element-components.card                 :as element-components.card]
              [x.app-elements.element-components.card-group           :as element-components.card-group]
              [x.app-elements.element-components.checkbox             :as element-components.checkbox]
              [x.app-elements.element-components.checkbox-group       :as element-components.checkbox-group]
              [x.app-elements.element-components.chip-group           :as element-components.chip-group]
              [x.app-elements.element-components.chip                 :as element-components.chip]
              [x.app-elements.element-components.circle-diagram       :as element-components.circle-diagram]
              [x.app-elements.color-selector.views                    :as color-selector.views]
              [x.app-elements.color-marker.views                      :as color-marker.views]
              [x.app-elements.color-stamp.views                       :as color-stamp.views]
              [x.app-elements.element-components.column               :as element-components.column]
              [x.app-elements.element-components.combo-box            :as element-components.combo-box]
              [x.app-elements.element-components.counter              :as element-components.counter]
              [x.app-elements.element-components.data-table           :as element-components.data-table]
              [x.app-elements.element-components.date-field           :as element-components.date-field]
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
              [x.app-elements.element-components.multiline-field      :as element-components.multiline-field]
              [x.app-elements.element-components.multi-combo-box      :as element-components.multi-combo-box]
              [x.app-elements.element-components.multi-field          :as element-components.multi-field]
              [x.app-elements.element-components.overlay              :as element-components.overlay]
              [x.app-elements.element-components.password-field       :as element-components.password-field]
              [x.app-elements.element-components.point-diagram        :as element-components.point-diagram]
              [x.app-elements.element-components.radio-button         :as element-components.radio-button]
              [x.app-elements.element-components.row                  :as element-components.row]
              [x.app-elements.element-components.search-field         :as element-components.search-field]
              [x.app-elements.select.views                            :as select.views]
              [x.app-elements.element-components.slideshow            :as element-components.slideshow]
              [x.app-elements.element-components.submit-button        :as element-components.submit-button]
              [x.app-elements.element-components.switch               :as element-components.switch]
              [x.app-elements.element-components.table                :as element-components.table]
              [x.app-elements.element-components.text                 :as element-components.text]
              [x.app-elements.element-components.text-field           :as element-components.text-field]
              [x.app-elements.thumbnail.views                         :as thumbnail.views]
              [x.app-elements.element-components.toggle               :as element-components.toggle]
              [x.app-elements.element-components.vertical-line        :as element-components.vertical-line]
              [x.app-elements.element-components.vertical-polarity    :as element-components.vertical-polarity]
              [x.app-elements.element-components.vertical-separator   :as element-components.vertical-separator]
              [x.app-elements.engine.api                              :as engine]))



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

; x.app-elements.engine.api
(def field-empty?           engine/field-empty?)
(def field-filled?          engine/field-filled?)
(def inputs-passed?         engine/inputs-passed?)
(def form-completed?        engine/form-completed?)
(def get-input-stored-value engine/get-input-stored-value)
(def get-input-value        engine/get-input-value)
(def reset-input-value!     engine/reset-input-value!)
(def clear-input-value!     engine/clear-input-value!)
(def get-field-value        engine/get-field-value)
(def set-element-prop!      engine/set-element-prop!)
(def update-element-prop!   engine/update-element-prop!)
(def remove-element-prop!   engine/remove-element-prop!)

; x.app-elements.element-components.*
(def anchor               anchor.views/element)
(def button               button.views/element)
(def button-separator     button-separator.views/element)
(def card                 element-components.card/element)
(def card-group           element-components.card-group/element)
(def checkbox             element-components.checkbox/element)
(def checkbox-group       element-components.checkbox-group/element)
(def chip-group           element-components.chip-group/element)
(def chip                 element-components.chip/element)
(def circle-diagram       element-components.circle-diagram/element)
(def color-selector       color-selector.views/element)
(def color-marker         color-marker.views/element)
(def color-stamp          color-stamp.views/element)
(def column               element-components.column/element)
(def combo-box            element-components.combo-box/element)
(def counter              element-components.counter/element)
(def data-table           element-components.data-table/element)
(def date-field           element-components.date-field/element)
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
(def multi-combo-box      element-components.multi-combo-box/element)
(def multi-field          element-components.multi-field/element)
(def multiline-field      element-components.multiline-field/element)
(def overlay              element-components.overlay/element)
(def password-field       element-components.password-field/element)
(def point-diagram        element-components.point-diagram/element)
(def radio-button         element-components.radio-button/element)
(def row                  element-components.row/element)
(def search-field         element-components.search-field/element)
(def select               select.views/element)
(def slideshow            element-components.slideshow/element)
(def submit-button        element-components.submit-button/element)
(def switch               element-components.switch/element)
(def table                element-components.table/element)
(def text                 element-components.text/element)
(def text-field           element-components.text-field/element)
(def thumbnail            thumbnail.views/element)
(def toggle               element-components.toggle/element)
(def vertical-line        element-components.vertical-line/element)
(def vertical-polarity    element-components.vertical-polarity/element)
(def vertical-separator   element-components.vertical-separator/element)
