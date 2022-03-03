
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.api
    (:require [x.app-elements.components.anchor               :as components.anchor]
              [x.app-elements.components.blank                :as components.blank]
              [x.app-elements.components.button               :as components.button]
              [x.app-elements.components.card                 :as components.card]
              [x.app-elements.components.card-group           :as components.card-group]
              [x.app-elements.components.checkbox             :as components.checkbox]
              [x.app-elements.components.checkbox-group       :as components.checkbox-group]
              [x.app-elements.components.chip-group           :as components.chip-group]
              [x.app-elements.components.chip                 :as components.chip]
              [x.app-elements.components.circle-diagram       :as components.circle-diagram]
              [x.app-elements.components.color-picker         :as components.color-picker]
              [x.app-elements.components.column               :as components.column]
              [x.app-elements.components.combo-box            :as components.combo-box]
              [x.app-elements.components.counter              :as components.counter]
              [x.app-elements.components.data-table           :as components.data-table]
              [x.app-elements.components.date-field           :as components.date-field]
              [x.app-elements.components.digit-field          :as components.digit-field]
              [x.app-elements.components.expandable           :as components.expandable]
              [x.app-elements.components.file-drop-area       :as components.file-drop-area]
              [x.app-elements.components.horizontal-line      :as components.horizontal-line]
              [x.app-elements.components.horizontal-polarity  :as components.horizontal-polarity]
              [x.app-elements.components.horizontal-separator :as components.horizontal-separator]
              [x.app-elements.components.icon                 :as components.icon]
              [x.app-elements.components.icon-button          :as components.icon-button]
              [x.app-elements.components.image                :as components.image]
              [x.app-elements.components.label                :as components.label]
              [x.app-elements.components.line-diagram         :as components.line-diagram]
              [x.app-elements.components.menu-bar             :as components.menu-bar]
              [x.app-elements.components.multiline-field      :as components.multiline-field]
              [x.app-elements.components.multi-combo-box      :as components.multi-combo-box]
              [x.app-elements.components.multi-field          :as components.multi-field]
              [x.app-elements.components.overlay              :as components.overlay]
              [x.app-elements.components.password-field       :as components.password-field]
              [x.app-elements.components.point-diagram        :as components.point-diagram]
              [x.app-elements.components.radio-button         :as components.radio-button]
              [x.app-elements.components.row                  :as components.row]
              [x.app-elements.components.search-field         :as components.search-field]
              [x.app-elements.components.select               :as components.select]
              [x.app-elements.components.slideshow            :as components.slideshow]
              [x.app-elements.components.submit-button        :as components.submit-button]
              [x.app-elements.components.switch               :as components.switch]
              [x.app-elements.components.table                :as components.table]
              [x.app-elements.components.text                 :as components.text]
              [x.app-elements.components.text-field           :as components.text-field]
              [x.app-elements.components.toggle               :as components.toggle]
              [x.app-elements.components.vertical-line        :as components.vertical-line]
              [x.app-elements.components.vertical-polarity    :as components.vertical-polarity]
              [x.app-elements.components.vertical-separator   :as components.vertical-separator]
              [x.app-elements.engine.api                      :as engine]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name optional element-id
;  Az egyes elemeket lehetséges element-id azonosító nélkül használni.
;  Azonban, ha az elem újra rendererelődik (Pl.: megváltoztatod az element-props térképet),
;  akkor az elem új azonosítót kap és az elem konstansként megjelelölt
;  tulajdonságai elérhetetlenné válnak az elem számára.
;
; @name constant
;  - Pl.: {:value-path (item-path vector)(constant)(opt)}
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
;  Az egyes input elemek React-fába történő csatolásakor az {:initial-value ...}
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

; x.app-elements.components.*
(def anchor               components.anchor/element)
(def blank                components.blank/element)
(def button               components.button/element)
(def card                 components.card/element)
(def card-group           components.card-group/element)
(def checkbox             components.checkbox/element)
(def checkbox-group       components.checkbox-group/element)
(def chip-group           components.chip-group/element)
(def chip                 components.chip/element)
(def circle-diagram       components.circle-diagram/element)
(def color-picker         components.color-picker/element)
(def column               components.column/element)
(def combo-box            components.combo-box/element)
(def counter              components.counter/element)
(def data-table           components.data-table/element)
(def date-field           components.date-field/element)
(def digit-field          components.digit-field/element)
(def expandable           components.expandable/element)
(def file-drop-area       components.file-drop-area/element)
(def horizontal-line      components.horizontal-line/element)
(def horizontal-polarity  components.horizontal-polarity/element)
(def horizontal-separator components.horizontal-separator/element)
(def icon                 components.icon/element)
(def icon-button          components.icon-button/element)
(def image                components.image/element)
(def label                components.label/element)
(def line-diagram         components.line-diagram/element)
(def menu-bar             components.menu-bar/element)
(def multi-combo-box      components.multi-combo-box/element)
(def multi-field          components.multi-field/element)
(def multiline-field      components.multiline-field/element)
(def overlay              components.overlay/element)
(def password-field       components.password-field/element)
(def point-diagram        components.point-diagram/element)
(def radio-button         components.radio-button/element)
(def row                  components.row/element)
(def search-field         components.search-field/element)
(def select               components.select/element)
(def slideshow            components.slideshow/element)
(def submit-button        components.submit-button/element)
(def switch               components.switch/element)
(def table                components.table/element)
(def text                 components.text/element)
(def text-field           components.text-field/element)
(def toggle               components.toggle/element)
(def vertical-line        components.vertical-line/element)
(def vertical-polarity    components.vertical-polarity/element)
(def vertical-separator   components.vertical-separator/element)
