
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
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-namespace @(a/subscribe [:item-editor/get-transfer-item editor-id :item-namespace])
        new-item-label  (core.helpers/new-item-label editor-id item-namespace)]
       [elements/label ::new-item-label
                       {:content new-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  [editor-id {:keys [name]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])]
       [elements/label ::named-item-label
                       {:content name :font-weight :extra-bold :font-size :l
                        :color (if editor-disabled? :highlight :default)}]))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-namespace    @(a/subscribe [:item-editor/get-transfer-item editor-id :item-namespace])
        unnamed-item-label (core.helpers/unnamed-item-label editor-id item-namespace)]
       [elements/label ::unnamed-item-label
                       {:content unnamed-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn item-label
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:name (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/item-label :my-editor {...}]
  [editor-id {:keys [name] :as element-props}]
  ; Az [item-label ...] komponens használatához szükséges a szótárhoz adni ...
  ; ... a  {:new-my-type     {...}} kifejezést!
  ; ... az {:unnamed-my-type {...}} kifejezést!
  (let [new-item? @(a/subscribe [:item-editor/new-item? editor-id])]
       (cond (string/nonempty? name) [named-item-label   editor-id element-props]
             (boolean new-item?)     [new-item-label     editor-id]
             :unnamed-item           [unnamed-item-label editor-id])))



;; -- Input components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-header
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/input-group-header :my-editor {...}]
  [editor-id {:keys [label]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])]
       [layouts/input-group-header {:label label :color (if editor-disabled? :highlight :default)}]))

(defn description-field
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/description-field :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        item-path        @(a/subscribe [:item-editor/get-body-prop    editor-id :item-path])
        value-path        (conj item-path :description)]
       [elements/multiline-field ::description-field
                                 {:value-path value-path :disabled? editor-disabled?}]))
