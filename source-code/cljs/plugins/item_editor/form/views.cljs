
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.form.views
    (:require [plugins.item-editor.core.helpers :as core.helpers]
              [mid-fruits.string                :as string]
              [x.app-core.api                   :as a]
              [x.app-elements.api               :as elements]
              [x.app-layouts.api                :as layouts]))



;; -- Form components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [new-item-label (core.helpers/new-item-label extension-id item-namespace)]
       [elements/label ::new-item-label
                       {:content new-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  [extension-id item-namespace {:keys [name]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/label ::named-item-label
                       {:content name :font-weight :extra-bold :font-size :l
                        :color (if editor-disabled? :highlight :default)}]))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [unnamed-item-label (core.helpers/unnamed-item-label extension-id item-namespace)]
       [elements/label ::unnamed-item-label
                       {:content unnamed-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn item-label
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:name (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/item-label :my-extension :my-type {...}]
  [extension-id item-namespace {:keys [name] :as element-props}]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       (cond (string/nonempty? name) [named-item-label   extension-id item-namespace element-props]
             (boolean new-item?)     [new-item-label     extension-id item-namespace]
             :unnamed-item           [unnamed-item-label extension-id item-namespace])))



;; -- Input components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/input-group-header :my-extension :my-type {...}]
  [extension-id item-namespace {:keys [label]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [layouts/input-group-header {:label label :color (if editor-disabled? :highlight :default)}]))

(defn description-field
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/description-field :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        item-path        @(a/subscribe [:item-editor/get-body-prop    extension-id item-namespace :item-path])
        value-path        (conj item-path :description)]
       [elements/multiline-field ::description-field
                                 {:value-path value-path :disabled? editor-disabled?}]))
