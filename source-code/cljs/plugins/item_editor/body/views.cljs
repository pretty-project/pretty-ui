
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.views
    (:require [plugins.item-editor.body.prototypes :as body.prototypes]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [mid-fruits.string                   :as string]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]
              [x.app-layouts.api                   :as layouts]))



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


;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/label {:font-size :xs :color :highlight :font-weight :bold :content :downloading...}])

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/row {:content [downloading-item-label editor-id]
                 :horizontal-align :center}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [_]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m    :color :warning}]
       [elements/label {:min-height :m :content :the-item-you-opened-may-be-broken :color :muted}]])

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [form-element @(a/subscribe [:item-editor/get-body-prop editor-id :form-element])]
       [form-element editor-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (cond @(a/subscribe [:item-editor/get-meta-item editor-id :error-mode?])
         [error-body editor-id]
        @(a/subscribe [:item-editor/body-did-mount? editor-id])
         (if-let [data-received? @(a/subscribe [:item-editor/get-meta-item editor-id :data-received?])]
                 [form-element     editor-id]
                 [downloading-item editor-id])))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :form-element (metamorphic-content)
  ;   :initial-item (map)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate, :revert, :save]
  ;   :item-id (string)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :new-item-id (string)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)
  ;   :suggestions-path (vector)(opt)
  ;    Default: core.helpers/default-suggestions-path}
  ;
  ; @usage
  ;  [item-editor/body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [editor-id] [:div ...])
  ;  [item-editor/body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [body-props (body.prototypes/body-props-prototype editor-id body-props)]
       (reagent/lifecycles (core.helpers/component-id editor-id :body)
                           {:reagent-render         (fn []             [body-structure                 editor-id])
                            :component-did-mount    (fn [] (a/dispatch [:item-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-editor/body-will-unmount editor-id]))})))
