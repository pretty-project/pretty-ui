
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.views
    (:require [mid-fruits.vector                   :as vector]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [plugins.item-editor.core.prototypes :as core.prototypes]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-item-icon-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/revert-item-icon-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        item-changed?    @(a/subscribe [:item-editor/item-changed?    editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/icon-button ::revert-item-icon-button
                        {:tooltip :restore! :preset :restore
                         :disabled? (or editor-disabled? error-mode? (not item-changed?))
                         :on-click  [:item-editor/revert-item! editor-id]}]))

(defn revert-item-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/revert-item-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        item-changed?    @(a/subscribe [:item-editor/item-changed?    editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/button ::revert-item-button
                        {:preset :restore-button :indent :both
                         :disabled? (or editor-disabled? error-mode? (not item-changed?))
                         :on-click  [:item-editor/revert-item! editor-id]}]))

(defn revert-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/revert-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [revert-item-icon-button editor-id]
          [revert-item-button      editor-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-icon-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/delete-item-icon-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/icon-button ::delete-item-icon-button
                        {:tooltip :delete! :preset :delete
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/delete-item! editor-id]}]))

(defn delete-item-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/delete-item-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/button ::delete-item-button
                        {:preset :delete-button :indent :both
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/delete-item! editor-id]}]))

(defn delete-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/delete-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [delete-item-icon-button editor-id]
          [delete-item-button      editor-id]))



;; -- Copy item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn copy-item-icon-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/copy-item-icon-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/icon-button ::copy-item-icon-button
                             {:tooltip :duplicate! :preset :duplicate
                              :disabled? (or editor-disabled? error-mode?)
                              :on-click  [:item-editor/duplicate-item! editor-id]}]))

(defn copy-item-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/copy-item-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])]
       [elements/button ::copy-item-button
                        {:preset :duplicate-button :indent :both
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/duplicate-item! editor-id]}]))

(defn copy-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/copy-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [copy-item-icon-button editor-id]
          [copy-item-button      editor-id]))



;; -- Save item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-item-icon-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/save-item-icon-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  editor-id])]
       [elements/icon-button ::save-item-icon-button
                             {:tooltip :save! :preset :save
                              :disabled? (or editor-disabled? error-mode? (not form-completed?))
                              :on-click  [:item-editor/save-item! editor-id]}]))

(defn save-item-button
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/save-item-button :my-editor]
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    editor-id :error-mode?])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  editor-id])]
       [elements/button ::save-item-button
                        {:preset :save-button :indent :both
                         :disabled? (or editor-disabled? error-mode? (not form-completed?))
                         :on-click  [:item-editor/save-item! editor-id]}]))

(defn save-item-block
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/save-item-block :my-editor]
  [editor-id]
  (if-let [viewport-small? @(a/subscribe [:environment/viewport-small?])]
          [save-item-icon-button editor-id]
          [save-item-button      editor-id]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-actions @(a/subscribe [:item-editor/get-body-prop editor-id :item-actions])]
       (if-let [new-item? @(a/subscribe [:item-editor/new-item? editor-id])]
               [:<>]
               [:<> (if (vector/contains-item? item-actions :delete)    [delete-item-block editor-id])
                    (if (vector/contains-item? item-actions :duplicate) [copy-item-block   editor-id])])))

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-actions @(a/subscribe [:item-editor/get-body-prop editor-id :item-actions])]
       [:<> (if (vector/contains-item? item-actions :revert) [revert-item-block editor-id])
            (if (vector/contains-item? item-actions :save)   [save-item-block   editor-id])]))

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/horizontal-polarity {:start-content [menu-start-buttons editor-id]
                                 :end-content   [menu-end-buttons   editor-id]}])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (if @(a/subscribe [:item-editor/header-did-mount? editor-id])
       (if-let [menu-element @(a/subscribe [:item-editor/get-header-prop editor-id :menu-element])]
               [menu-element     editor-id]
               [menu-mode-header editor-id])))

(defn header
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:menu-element (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/header :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [editor-id] [:div ...])
  ;  [item-editor/header :my-editor {:menu #'my-menu-element}]
  [editor-id header-props]
  (reagent/lifecycles (core.helpers/component-id editor-id :header)
                      {:reagent-render         (fn []             [header-structure                 editor-id])
                       :component-did-mount    (fn [] (a/dispatch [:item-editor/header-did-mount    editor-id header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-editor/header-will-unmount editor-id]))}))



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
  [:<> ;[elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
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
  (let [body-props (core.prototypes/body-props-prototype editor-id body-props)]
       (reagent/lifecycles (core.helpers/component-id editor-id :body)
                           {:reagent-render         (fn []             [body-structure                 editor-id])
                            :component-did-mount    (fn [] (a/dispatch [:item-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-editor/body-will-unmount editor-id]))})))
