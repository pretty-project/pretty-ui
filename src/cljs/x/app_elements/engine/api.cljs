
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.6.2
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.api
    (:require [x.app-elements.engine.checkable          :as checkable]
              [x.app-elements.engine.clickable          :as clickable]
              [x.app-elements.engine.collectable        :as collectable]
              [x.app-elements.engine.combo-box          :as combo-box]
              [x.app-elements.engine.context-surface    :as context-surface]
              [x.app-elements.engine.countable          :as countable]
              [x.app-elements.engine.deletable          :as deletable]
              [x.app-elements.engine.element            :as element]
              [x.app-elements.engine.element-adornments :as element-adornments]
              [x.app-elements.engine.element-info       :as element-info]
              [x.app-elements.engine.element-stickers   :as element-stickers]
              [x.app-elements.engine.expandable         :as expandable]
              [x.app-elements.engine.field              :as field]
              [x.app-elements.engine.focusable          :as focusable]
              [x.app-elements.engine.form               :as form]
              [x.app-elements.engine.input              :as input]
              [x.app-elements.engine.input-group        :as input-group]
              [x.app-elements.engine.passfield          :as passfield]
              [x.app-elements.engine.preset             :as preset]
              [x.app-elements.engine.selectable         :as selectable]
              [x.app-elements.engine.stated-element     :as stated-element]
              [x.app-elements.engine.steppable          :as steppable]
              [x.app-elements.engine.surface            :as surface]
              [x.app-elements.engine.targetable         :as targetable]
              [x.app-elements.engine.visible            :as visible]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.engine.checkable
(def checkable-attributes      checkable/checkable-attributes)
(def checkable-body-attributes checkable/checkable-body-attributes)
(def get-checkable-view-props  checkable/get-checkable-view-props)

; x.app-elements.engine.combo-box
(def view-props->render-option?    combo-box/view-props->render-option?)
(def view-props->rendered-options  combo-box/view-props->rendered-options)
(def view-props->value-extendable? combo-box/view-props->value-extendable?)
(def view-props->render-extender?  combo-box/view-props->render-extender?)
(def view-props->render-options?   combo-box/view-props->render-options?)
(def get-combo-box-view-props      combo-box/get-combo-box-view-props)

; x.app-elements.engine.countable
(def get-countable-view-props countable/get-countable-view-props)

; x.app-elements.engine.clickable
(def clickable-body-attributes clickable/clickable-body-attributes)

; x.app-elements.engine.countable
(def countable-decrease-attributes countable/countable-decrease-attributes)
(def countable-increase-attributes countable/countable-increase-attributes)
(def countable-reset-attributes    countable/countable-reset-attributes)

; x.app-elements.engine.deletable
(def deletable-body-attributes deletable/deletable-body-attributes)

; x.app-elements.engine.element
(def view-props->element-selected?         element/view-props->element-selected?)
(def element-id->extended-id               element/element-id->extended-id)
(def element-props->render-element-header? element/element-props->render-element-header?)
(def element-props-path                    element/element-props-path)
(def element-prop-path                     element/element-prop-path)
(def element-attributes                    element/element-attributes)
(def get-element-prop                      element/get-element-prop)
(def get-element-props                     element/get-element-props)
(def get-element-view-props                element/get-element-view-props)
(def set-element-prop!                     element/set-element-prop!)
(def update-element-prop!                  element/update-element-prop!)
(def remove-element-prop!                  element/remove-element-prop!)

; x.app-elements.engine.element-adornments
(def element-end-adornments   element-adornments/element-end-adornments)
(def element-start-adornments element-adornments/element-start-adornments)

; x.app-elements.engine.element-info
(def element-helper       element-info/element-helper)
(def element-info-tooltip element-info/element-info-tooltip)

; x.app-elements.engine.element-stickers
(def element-stickers element-stickers/element-stickers)

; x.app-elements.engine.field
(def empty-field-adornment-preset          field/empty-field-adornment-preset)
(def reset-field-adornment-preset          field/reset-field-adornment-preset)
(def view-props->render-field-placeholder? field/view-props->render-field-placeholder?)
(def field-body-attributes                 field/field-body-attributes)
(def field-placeholder-attributes          field/field-placeholder-attributes)
(def field-empty?                          field/field-empty?)
(def field-filled?                         field/field-filled?)
(def get-field-value                       field/get-field-value)
(def get-field-view-props                  field/get-field-view-props)

; x.app-elements.engine.focusable
(def blur-element-function focusable/blur-element-function)

; x.app-elements.engine.form
(def inputs-passed?  form/inputs-passed?)
(def form-completed? form/form-completed?)

; x.app-elements.engine.input
(def default-options-path   input/default-options-path)
(def default-value-path     input/default-value-path)
(def generate-value-path    input/generate-value-path)
(def get-input-stored-value input/get-input-stored-value)
(def get-input-value        input/get-input-value)
(def get-input-view-props   input/get-input-view-props)

; x.app-elements.engine.input-group
(def get-input-group-view-props input-group/get-input-group-view-props)

; x.app-elements.engine.preset
(def apply-preset preset/apply-preset)

; x.app-elements.engine.passfield
(def get-passfield-view-props passfield/get-passfield-view-props)

; x.app-elements.engine.selectable
(def on-select-function             selectable/on-select-function)
(def on-unselect-function           selectable/on-unselect-function)
(def selectable-attributes          selectable/selectable-attributes)
(def selectable-option-attributes   selectable/selectable-option-attributes)
(def selectable-unselect-attributes selectable/selectable-unselect-attributes)
(def get-selectable-view-props      selectable/get-selectable-view-props)

; x.app-elements.engine.stated-element
(def stated-element stated-element/view)

; x.app-elements.engine.steppable
(def steppable-attributes     steppable/steppable-attributes)
(def get-steppable-view-props steppable/get-steppable-view-props)

; x.app-elements.engine.surface
(def get-surface-view-props surface/get-surface-view-props)

; x.app-elements.engine.visible
(def on-hide-function                visible/on-hide-function)
(def on-show-function                visible/on-show-function)
(def visible-items->first-content-id visible/visible-items->first-content-id)
(def get-visible-view-props          visible/get-visible-view-props)
