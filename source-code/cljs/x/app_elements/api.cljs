
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v1.1.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.api
    (:require [x.app-elements.anchor            :as anchor]
              [x.app-elements.blank             :as blank]
              [x.app-elements.button            :as button]
              [x.app-elements.card              :as card]
              [x.app-elements.card-group        :as card-group]
              [x.app-elements.checkbox          :as checkbox]
              [x.app-elements.chip-group        :as chip-group]
              [x.app-elements.chip              :as chip]
              [x.app-elements.circle-diagram    :as circle-diagram]
              [x.app-elements.column            :as column]
              [x.app-elements.combo-box         :as combo-box]
              [x.app-elements.counter           :as counter]
              [x.app-elements.data-table        :as data-table]
              [x.app-elements.date-field        :as date-field]
              [x.app-elements.digit-field       :as digit-field]

              ; WARNING! DEPRECATED! DO NOT USE!
              [x.app-elements.directory         :as directory]
              [x.app-elements.file              :as file]
              ; WARNING! DEPRECATED! DO NOT USE!

              [x.app-elements.engine.api        :as engine]
              [x.app-elements.expandable        :as expandable]
              [x.app-elements.file-drop-area    :as file-drop-area]
              [x.app-elements.horizontal-line   :as horizontal-line]
              [x.app-elements.icon              :as icon]
              [x.app-elements.image             :as image]
              [x.app-elements.label             :as label]
              [x.app-elements.line-diagram      :as line-diagram]
              [x.app-elements.menu-bar          :as menu-bar]
              [x.app-elements.multiline-field   :as multiline-field]
              [x.app-elements.multi-combo-box   :as multi-combo-box]
              [x.app-elements.multi-field       :as multi-field]
              [x.app-elements.overlay           :as overlay]
              [x.app-elements.password-field    :as password-field]
              [x.app-elements.polarity          :as polarity]
              [x.app-elements.radio-button      :as radio-button]
              [x.app-elements.row               :as row]
              [x.app-elements.search-field      :as search-field]
              [x.app-elements.select            :as select]
              [x.app-elements.separator         :as separator]
              [x.app-elements.slideshow         :as slideshow]
              [x.app-elements.submit-button     :as submit-button]
              [x.app-elements.switch            :as switch]
              [x.app-elements.table             :as table]
              [x.app-elements.text              :as text]
              [x.app-elements.text-field        :as text-field]
              [x.app-elements.toggle            :as toggle]
              [x.app-elements.vertical-line     :as vertical-line]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name optional element-id
;  Az egyes elemeket lehetséges element-id azonosító nélkül használni.
;  Azonban, ha az elem újra rendererelődik (Pl.: megváltoztatod az element-props térképet),
;  akkor az elem új azonosítót kap és az elem konstansként megjelelölt
;  tulajdonságai elérhetetlenné válnak az elem számára.
;
; @name constant
;  Pl.: {:value-path (item-path vector)(constant)(opt)}
;  Az elemek azon tulajdonságai konstansak, amelyek a Re-Frame adatbázisba
;  kerülnek az elem React-fába való csatolásakor.
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
;  XXX#7610
;  Az [x.app-components/content] komponens névterében rögzített szabvány szerint
;  lehetséges a {:content ... :content-props {...} :subscriber [...]} tulajdonságokat
;  fogadó elemeket használni.
;
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
(def get-field-value        engine/get-field-value)
(def set-element-prop!      engine/set-element-prop!)
(def update-element-prop!   engine/update-element-prop!)
(def remove-element-prop!   engine/remove-element-prop!)
(def blur-element-function  engine/blur-element-function)

; x.app-elements.*
(def anchor            anchor/element)
(def blank             blank/element)
(def button            button/element)
(def card              card/element)
(def card-group        card-group/element)
(def checkbox          checkbox/element)
(def chip-group        chip-group/element)
(def chip              chip/element)
(def circle-diagram    circle-diagram/element)
(def column            column/element)
(def combo-box         combo-box/element)
(def counter           counter/element)
(def data-table        data-table/element)
(def date-field        date-field/element)
(def digit-field       digit-field/element)

; WARNING! DEPRECATED! DO NOT USE!
(def directory         directory/view)
(def file              file/view)
; WARNING! DEPRECATED! DO NOT USE!

(def expandable        expandable/element)
(def file-drop-area    file-drop-area/element)
(def horizontal-line   horizontal-line/element)
(def icon              icon/element)
(def image             image/element)
(def label             label/element)
(def line-diagram      line-diagram/element)
(def menu-bar          menu-bar/element)
(def multi-combo-box   multi-combo-box/element)
(def multi-field       multi-field/element)
(def multiline-field   multiline-field/element)
(def overlay           overlay/element)
(def password-field    password-field/element)
(def polarity          polarity/element)
(def radio-button      radio-button/element)
(def row               row/element)
(def search-field      search-field/element)
(def select            select/element)
(def separator         separator/element)
(def slideshow         slideshow/element)
(def submit-button     submit-button/element)
(def switch            switch/element)
(def table             table/element)
(def text              text/element)
(def text-field        text-field/element)
(def toggle            toggle/element)
(def vertical-line     vertical-line/element)
