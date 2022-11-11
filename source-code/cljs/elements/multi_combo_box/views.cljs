
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.multi-combo-box.views
    (:require [elements.chip-group.views           :as chip-group.views]
              [elements.combo-box.views            :as combo-box.views]
              [elements.multi-combo-box.helpers    :as multi-combo-box.helpers]
              [elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]
              [elements.text-field.views           :as text-field.views]
              [mid-fruits.random                   :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [group-id    (multi-combo-box.helpers/box-id->group-id         box-id)
        group-props (multi-combo-box.prototypes/group-props-prototype box-id box-props)]
       [chip-group.views/element group-id group-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [field-id    (multi-combo-box.helpers/box-id->field-id         box-id)
        field-props (multi-combo-box.prototypes/field-props-prototype box-id box-props)]
       [combo-box.views/combo-box field-id field-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div.e-multi-combo-box (multi-combo-box.helpers/box-attributes box-id box-props)
                          [text-field.views/text-field-label      box-id box-props]
                          [multi-combo-box-chip-group             box-id box-props]
                          [multi-combo-box-field                  box-id box-props]])

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; A multi-combo-box elem alapkomponense a text-field elem.
  ; A multi-combo-box elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:chip-label-f (function)(opt)
  ;    Default: return
  ;   :field-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :option-component (component)(opt)
  ;    Default: elements.combo-box/default-option-component
  ;   :options (vector)(opt)
  ;   :options-path (vector)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [multi-combo-box {...}]
  ;
  ; @usage
  ;  [multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (multi-combo-box.prototypes/box-props-prototype  box-id box-props)
         box-props (multi-combo-box.prototypes/box-events-prototype box-id box-props)
         box-props (assoc box-props :surface [combo-box.views/combo-box-surface box-id box-props])]
        [multi-combo-box box-id box-props])))
