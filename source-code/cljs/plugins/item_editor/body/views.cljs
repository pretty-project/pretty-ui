
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.views
    (:require [plugins.item-editor.body.prototypes :as body.prototypes]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [plugins.plugin-handler.body.views   :as body.views]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.views
(def error-body body.views/error-body)



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
