
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.api
    (:require [x.app-elements.engine.clickable          :as clickable]
              [x.app-elements.engine.element            :as element]
              [x.app-elements.engine.element-badge      :as element-badge]
              [x.app-elements.engine.field              :as field]
              [x.app-elements.engine.input-group        :as input-group]
              [x.app-elements.engine.selectable         :as selectable]
              [x.app-elements.engine.stated-element     :as stated-element]
              [x.app-elements.engine.steppable          :as steppable]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.engine.clickable
(def clickable-body-attributes clickable/clickable-body-attributes)

; x.app-elements.engine.element
(def element-attributes         element/element-attributes)
(def get-element-prop           element/get-element-prop)
(def get-element-props          element/get-element-props)
(def set-element-prop!          element/set-element-prop!)
(def update-element-prop!       element/update-element-prop!)
(def remove-element-prop!       element/remove-element-prop!)

; x.app-elements.engine.element-badge
(def element-badge element-badge/element-badge)

; x.app-elements.engine.field
(def field-empty?                           field/field-empty?)
(def field-filled?                          field/field-filled?)
(def field-focused?                         field/field-focused?)
(def get-field-value                        field/get-field-value)
(def get-field-props                        field/get-field-props)
(def empty-field-value!                     field/empty-field-value!)

; x.app-elements.engine.input-group
(def get-input-group-value input-group/get-input-group-value)
(def get-input-group-props input-group/get-input-group-props)
(def decrease-input-count! input-group/decrease-input-count!)
(def increase-input-count! input-group/increase-input-count!)

; x.app-elements.engine.selectable
(def on-select-function              selectable/on-select-function)
(def on-unselect-function            selectable/on-unselect-function)
(def selectable-attributes           selectable/selectable-attributes)
(def selectable-option-attributes    selectable/selectable-option-attributes)
(def selectable-unselect-attributes  selectable/selectable-unselect-attributes)
(def get-selectable-props            selectable/get-selectable-props)
(def add-option!                     selectable/add-option!)
(def select-option!                  selectable/select-option!)

; x.app-elements.engine.stated-element
(def stated-element stated-element/element)

; x.app-elements.engine.steppable
(def steppable-attributes steppable/steppable-attributes)
(def get-steppable-props  steppable/get-steppable-props)
