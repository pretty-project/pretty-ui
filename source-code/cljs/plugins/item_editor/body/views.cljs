
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.views
    (:require [plugins.item-editor.body.prototypes :as body.prototypes]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [mid-fruits.string                   :as string]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.views
(def error-body body.views/error-body)



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
                       {:color       :highlight
                        :content     new-item-label
                        :font-size   :l
                        :font-weight :extra-bold}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  [editor-id {:keys [name]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        color             (if editor-disabled? :highlight :default)]
       [elements/label ::named-item-label
                       {:color       color
                        :content     name
                        :font-size   :l
                        :font-weight :extra-bold}]))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-namespace    @(a/subscribe [:item-editor/get-transfer-item editor-id :item-namespace])
        unnamed-item-label (core.helpers/unnamed-item-label editor-id item-namespace)]
       [elements/label ::unnamed-item-label
                       {:color       :highlight
                        :content     unnamed-item-label
                        :font-size   :l
                        :font-weight :extra-bold}]))

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



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [_]
  ; TEMP
  [:div {:style {:width "100%"}}
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        ;[:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
        ;      [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
        ;      [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px" :justify-content "center"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "72px" :width "50%"}}]]])
  ; TEMP



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

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
         [error-body editor-id {:error-description :the-item-you-opened-may-be-broken}]
        @(a/subscribe [:item-editor/data-received? editor-id])
         [form-element editor-id]
        :data-not-received
         [downloading-item editor-id]))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:label-key ...}
  ;   :default-item-id (string)
  ;   :form-element (metamorphic-content)
  ;   :initial-item (map)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :label-key (keyword)(opt)
  ;    Only w/ {:auto-title? true}
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
                           {:reagent-render         (fn []              [body-structure                 editor-id])
                            :component-did-mount    (fn []  (a/dispatch [:item-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn []  (a/dispatch [:item-editor/body-will-unmount editor-id]))
                            :component-did-update   (fn [%] (a/dispatch [:item-editor/body-did-update   editor-id %]))})))
