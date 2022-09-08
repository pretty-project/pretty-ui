
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.views
    (:require [x.app-core.api                            :as a]
              [x.app-elements.chip-group.views           :as chip-group.views]
              [x.app-elements.combo-box.views            :as combo-box.views]
              [x.app-elements.engine.api                 :as engine]
              [x.app-elements.multi-combo-box.helpers    :as multi-combo-box.helpers]
              [x.app-elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]))



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
  [:div.x-multi-combo-box (multi-combo-box.helpers/box-attributes box-id box-props)
                          [engine/element-header                  box-id box-props]
                          [multi-combo-box-chip-group             box-id box-props]
                          [multi-combo-box-field                  box-id box-props]])

(defn element
  ; XXX#0711
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
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box/default-option-component
  ;   :options (vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/multi-combo-box {...}]
  ;
  ; @usage
  ;  [elements/multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [element (a/id) box-props])

  ([box-id box-props]
   (let [box-props (multi-combo-box.prototypes/box-props-prototype  box-id box-props)
         box-props (multi-combo-box.prototypes/box-events-prototype box-id box-props)
         box-props (assoc box-props :surface [combo-box.views/combo-box-surface box-id box-props])]
        [multi-combo-box box-id box-props])))
