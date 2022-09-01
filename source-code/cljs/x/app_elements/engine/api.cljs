
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.api
    (:require [x.app-elements.engine.checkable          :as checkable]
              [x.app-elements.engine.clickable          :as clickable]
              [x.app-elements.engine.combo-box          :as combo-box]
              [x.app-elements.engine.countable          :as countable]
              [x.app-elements.engine.element            :as element]
              [x.app-elements.engine.element-badge      :as element-badge]
              [x.app-elements.engine.element-info       :as element-info]
              [x.app-elements.engine.field              :as field]
              [x.app-elements.engine.input-group        :as input-group]
              [x.app-elements.engine.presets            :as presets]
              [x.app-elements.engine.selectable         :as selectable]
              [x.app-elements.engine.stated-element     :as stated-element]
              [x.app-elements.engine.steppable          :as steppable]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.engine.checkable
(def checkable-attributes                checkable/checkable-attributes)
(def checkable-primary-body-attributes   checkable/checkable-primary-body-attributes)
(def checkable-secondary-body-attributes checkable/checkable-secondary-body-attributes)
(def get-checkable-value                 checkable/get-checkable-value)
(def checkable-checked?                  checkable/checkable-checked?)
(def checkable-nonchecked?               checkable/checkable-nonchecked?)
(def get-checkable-props                 checkable/get-checkable-props)

; x.app-elements.engine.combo-box
(def get-combo-box-props combo-box/get-combo-box-props)

; x.app-elements.engine.countable
(def countable-decrease-attributes countable/countable-decrease-attributes)
(def countable-increase-attributes countable/countable-increase-attributes)
(def countable-reset-attributes    countable/countable-reset-attributes)
(def get-countable-props           countable/get-countable-props)

; x.app-elements.engine.clickable
(def clickable-body-attributes clickable/clickable-body-attributes)

; x.app-elements.engine.element
(def apply-color                element/apply-color)
(def apply-dimension            element/apply-dimension)
(def element-default-attributes element/element-default-attributes)
(def element-indent-attributes  element/element-indent-attributes)
(def element-attributes         element/element-attributes)
(def get-element-prop           element/get-element-prop)
(def get-element-props          element/get-element-props)
(def set-element-prop!          element/set-element-prop!)
(def update-element-prop!       element/update-element-prop!)
(def remove-element-prop!       element/remove-element-prop!)

; x.app-elements.engine.element-badge
(def element-badge element-badge/element-badge)

; x.app-elements.engine.element-info
(def element-helper element-info/element-helper)
(def element-header element-info/element-header)

; TEMP
(def info-text-content element-info/info-text-content)
(def info-text-button  element-info/info-text-button)
; TEMP

; x.app-elements.engine.field
(def field-props->render-field-placeholder? field/field-props->render-field-placeholder?)
(def field-empty?                           field/field-empty?)
(def field-filled?                          field/field-filled?)
(def field-focused?                         field/field-focused?)
(def get-field-value                        field/get-field-value)
(def get-field-props                        field/get-field-props)
(def empty-field-value!                     field/empty-field-value!)

; x.app-elements.engine.input-group
(def get-input-group-props input-group/get-input-group-props)

; x.app-elements.engine.presets
(def apply-preset presets/apply-preset)

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
