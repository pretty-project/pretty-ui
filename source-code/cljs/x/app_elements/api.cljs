
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.api
    (:require [x.app-elements.components.anchor               :as anchor]
              [x.app-elements.components.blank                :as blank]
              [x.app-elements.components.button               :as button]
              [x.app-elements.components.card                 :as card]
              [x.app-elements.components.card-group           :as card-group]
              [x.app-elements.components.checkbox             :as checkbox]
              [x.app-elements.components.checkbox-group       :as checkbox-group]
              [x.app-elements.components.chip-group           :as chip-group]
              [x.app-elements.components.chip                 :as chip]
              [x.app-elements.components.circle-diagram       :as circle-diagram]
              [x.app-elements.components.color-picker         :as color-picker]
              [x.app-elements.components.column               :as column]
              [x.app-elements.components.combo-box            :as combo-box]
              [x.app-elements.components.counter              :as counter]
              [x.app-elements.components.data-table           :as data-table]
              [x.app-elements.components.date-field           :as date-field]
              [x.app-elements.components.digit-field          :as digit-field]
              [x.app-elements.components.engine.api           :as engine]
              [x.app-elements.components.expandable           :as expandable]
              [x.app-elements.components.file-drop-area       :as file-drop-area]
              [x.app-elements.components.horizontal-line      :as horizontal-line]
              [x.app-elements.components.horizontal-polarity  :as horizontal-polarity]
              [x.app-elements.components.horizontal-separator :as horizontal-separator]
              [x.app-elements.components.icon                 :as icon]
              [x.app-elements.components.icon-button          :as icon-button]
              [x.app-elements.components.image                :as image]
              [x.app-elements.components.label                :as label]
              [x.app-elements.components.line-diagram         :as line-diagram]
              [x.app-elements.components.menu-bar             :as menu-bar]
              [x.app-elements.components.multiline-field      :as multiline-field]
              [x.app-elements.components.multi-combo-box      :as multi-combo-box]
              [x.app-elements.components.multi-field          :as multi-field]
              [x.app-elements.components.overlay              :as overlay]
              [x.app-elements.components.password-field       :as password-field]
              [x.app-elements.components.point-diagram        :as point-diagram]
              [x.app-elements.components.radio-button         :as radio-button]
              [x.app-elements.components.row                  :as row]
              [x.app-elements.components.search-field         :as search-field]
              [x.app-elements.components.select               :as select]
              [x.app-elements.components.slideshow            :as slideshow]
              [x.app-elements.components.submit-button        :as submit-button]
              [x.app-elements.components.switch               :as switch]
              [x.app-elements.components.table                :as table]
              [x.app-elements.components.text                 :as text]
              [x.app-elements.components.text-field           :as text-field]
              [x.app-elements.components.toggle               :as toggle]
              [x.app-elements.components.vertical-line        :as vertical-line]
              [x.app-elements.components.vertical-polarity    :as vertical-polarity]
              [x.app-elements.components.vertical-separator   :as vertical-separator]))



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

; x.app-elements.*
(def anchor               anchor/element)
(def blank                blank/element)
(def button               button/element)
(def card                 card/element)
(def card-group           card-group/element)
(def checkbox             checkbox/element)
(def checkbox-group       checkbox-group/element)
(def chip-group           chip-group/element)
(def chip                 chip/element)
(def circle-diagram       circle-diagram/element)
(def color-picker         color-picker/element)
(def column               column/element)
(def combo-box            combo-box/element)
(def counter              counter/element)
(def data-table           data-table/element)
(def date-field           date-field/element)
(def digit-field          digit-field/element)
(def expandable           expandable/element)
(def file-drop-area       file-drop-area/element)
(def horizontal-line      horizontal-line/element)
(def horizontal-polarity  horizontal-polarity/element)
(def horizontal-separator horizontal-separator/element)
(def icon                 icon/element)
(def icon-button          icon-button/element)
(def image                image/element)
(def label                label/element)
(def line-diagram         line-diagram/element)
(def menu-bar             menu-bar/element)
(def multi-combo-box      multi-combo-box/element)
(def multi-field          multi-field/element)
(def multiline-field      multiline-field/element)
(def overlay              overlay/element)
(def password-field       password-field/element)
(def point-diagram        point-diagram/element)
(def radio-button         radio-button/element)
(def row                  row/element)
(def search-field         search-field/element)
(def select               select/element)
(def slideshow            slideshow/element)
(def submit-button        submit-button/element)
(def switch               switch/element)
(def table                table/element)
(def text                 text/element)
(def text-field           text-field/element)
(def toggle               toggle/element)
(def vertical-line        vertical-line/element)
(def vertical-polarity    vertical-polarity/element)
(def vertical-separator   vertical-separator/element)
