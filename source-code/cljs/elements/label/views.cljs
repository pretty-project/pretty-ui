
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.label.views
    (:require [candy.api                 :refer [param]]
              [elements.label.helpers    :as label.helpers]
              [elements.label.prototypes :as label.prototypes]
              [mid-fruits.random         :as random]
              [x.app-components.api      :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-asterisk
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {}
  [_ {:keys [required?]}]
  ; Ha az elem {:required? ...} tulajdonságának értéke :unmarked, akkor az elem
  ; {:required? true} állapotban van, tehát többek közt az engine/input-passed?
  ; függvény kötelező inputnak tekinti, de közben a {:required? true} állapotot jelölő
  ; piros csillag és az input kitöltésésére figyelmeztető piros szöveg nem jelenik meg.
  ;
  ; Pl. A bejelentkező képernyőn lévő email-address és password mezők {:required? true}
  ;      állapotban kell, hogy legyenek, hogy a login submit-button {:disabled? true}
  ;      állapotban lehessen mindaddig, amíg a mezők nincsenek kitöltve, miközben
  ;      a mezőkön nem jelennek meg {:required? true} állapotra utaló jelölések.
  (if (true? required?)
      [:span.e-label--asterisk "*"]))

(defn- label-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  (if helper [:div.e-label--helper (x.components/content helper)]))

(defn label-info-text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text]}]
  (if info-text (if-let [info-text-visible? (label.helpers/info-text-visible? label-id)]
                        [:div.e-label--info-text (x.components/content info-text)])))

(defn label-info-text-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:info-text (metamorphic-content)(opt)}
  [label-id {:keys [info-text] :as label-props}]
  (if info-text [:button.e-label--info-text-button (label.helpers/label-info-text-button-attributes label-id label-props)
                                                   (param :info_outline)]))

(defn- label-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:icon (keyword)
  ;   :icon-family (keyword)}
  [_ {:keys [icon icon-family]}]
  [:i.e-label--icon {:data-icon-family icon-family} icon])

(defn- label-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  [:div.e-label--placeholder {:data-selectable false}
                             (if placeholder (x.components/content placeholder)
                                             "\u00A0")])

(defn- label-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:content (metamorphic-content)
  ;   :target-id (keyword)(opt)}
  [label-id {:keys [content target-id]}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  [:label.e-label--content {:for target-id}
                           (x.components/content content)])

(defn- label-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {:content (metamorphic-content)
  ;   :icon (keyword)(opt)
  ;   :selectable? (boolean)}
  [label-id {:keys [content icon selectable?] :as label-props}]
  ; XXX#9811
  ; Egyes esetekben a megjelenített szöveg értéke egy üres string, amíg
  ; a tényleges érték, nem töltődik le vagy nem töltődik be.
  ; Ilyenkor ha nem lenne minden esetben placeholder alkalmazva, akkor 0px magasságú lenne
  ; a label elem a letöltődés/betöltődés idejére, ami az alatta megjelenített tartalom
  ; esetleges ugrását okozná (a szöveg tényleges megjelenésekor)!
  [:div.e-label--body (label.helpers/label-body-attributes label-id label-props)
                      ; BUG#3400
                      (if (-> content x.components/content str empty?)
                          [label-placeholder label-id label-props]
                          [:<> (if icon [label-icon label-id label-props])
                               [label-content  label-id label-props]
                               [label-asterisk label-id label-props]
                               (if icon [:div.e-label--icon-placeholder])
                               [label-info-text-button label-id label-props]])])

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  [label-id label-props]
  [:div.e-label (label.helpers/label-attributes label-id label-props)
                [label-body                     label-id label-props]
                [label-info-text                label-id label-props]
                [label-helper                   label-id label-props]])

(defn element
  ; XXX#0439
  ; A label elemen megjelenített szöveg nem törik meg akkor sem, ha nincs elegendő hely.
  ; A text elemen megjelenített szöveg megtörik, ha nincs elegendő hely.
  ;
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;    Default: :s
  ;   :font-weight (keyword)(opt)
  ;    :bold, extra-bold, :inherit, :normal
  ;    Default :bold
  ;   :horizontal-align (keyword)(opt)
  ;    :center, :left, :right
  ;    Default: :left
  ;   :horizontal-position (keyword)(opt)
  ;    :center, :left, :none, :right
  ;    Default: :none
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :info-text (metamorphic-content)(opt)
  ;   :line-height (keyword)(opt)
  ;    :block, :normal
  ;    Default: :normal
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :placeholder (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :selectable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :target-id (keyword)(opt)
  ;   :vertical-position (keyword)(opt)
  ;    :bottom, :center, :none, :top
  ;    Default: :none}
  ;
  ; @usage
  ;  [label {...}]
  ;
  ; @usage
  ;  [label :my-label {...}]
  ([label-props]
   [element (random/generate-keyword) label-props])

  ([label-id label-props]
   (let [label-props (label.prototypes/label-props-prototype label-props)]
        [label label-id label-props])))
