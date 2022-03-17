
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.views
    (:require [app-fruits.reagent                  :as reagent]
              [mid-fruits.vector                   :as vector]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [plugins.item-editor.core.prototypes :as core.prototypes]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/delete-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    extension-id item-namespace :error-mode?])]
       [elements/button ::delete-item-button
                        {:tooltip :delete! :preset :delete-icon-button
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/delete-item! extension-id item-namespace]}]))

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/copy-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    extension-id item-namespace :error-mode?])]
       [elements/button ::copy-item-button
                        {:tooltip :duplicate! :preset :duplicate-icon-button
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/duplicate-item! extension-id item-namespace]}]))

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/save-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/get-meta-item    extension-id item-namespace :error-mode?])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  extension-id item-namespace])]
       [elements/button ::save-item-button
                        {:tooltip :save! :preset :save-icon-button
                         :disabled? (or (not form-completed?) editor-disabled? error-mode?)
                         :on-click [:item-editor/save-item! extension-id item-namespace]}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [item-actions @(a/subscribe [:item-editor/get-body-prop extension-id item-namespace :item-actions])]
       (if-let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
               [:<> (if (vector/contains-item? item-actions :save)      [save-item-button extension-id item-namespace])]
               [:<> (if (vector/contains-item? item-actions :save)      [save-item-button extension-id item-namespace])
                    (if (vector/contains-item? item-actions :duplicate) [copy-item-button extension-id item-namespace])])))

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [item-actions @(a/subscribe [:item-editor/get-body-prop extension-id item-namespace :item-actions])]
       (if-let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
               [:<>]
               [:<> (if (vector/contains-item? item-actions :delete) [delete-item-button extension-id item-namespace])])))

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/horizontal-polarity {:start-content [menu-start-buttons extension-id item-namespace]
                                 :end-content   [menu-end-buttons   extension-id item-namespace]}])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if @(a/subscribe [:item-editor/header-did-mount? extension-id item-namespace])
       (if-let [menu-element @(a/subscribe [:item-editor/get-header-prop extension-id item-namespace :menu-element])]
               [menu-element     extension-id item-namespace]
               [menu-mode-header extension-id item-namespace])))

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu-element (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/header :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [extension-id item-namespace] [:div ...])
  ;  [item-editor/header :my-extension :my-type {:menu #'my-menu-element}]
  [extension-id item-namespace header-props]
  (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :header)
                      {:reagent-render         (fn []             [header-structure                 extension-id item-namespace])
                       :component-did-mount    (fn [] (a/dispatch [:item-editor/header-did-mount    extension-id item-namespace header-props]))
                       :component-will-unmount (fn [] (a/dispatch [:item-editor/header-will-unmount extension-id item-namespace]))}))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/label {:font-size :xs :color :highlight :font-weight :bold :content :downloading...}])

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/row {:content [downloading-item-label extension-id item-namespace]
                 :horizontal-align :center}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [_ _]
  [:<> ;[elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-item-you-opened-may-be-broken :color :muted}]])

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [form-element @(a/subscribe [:item-editor/get-body-prop extension-id item-namespace :form-element])]
       [form-element extension-id item-namespace]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if-let [error-mode? @(a/subscribe [:item-editor/get-meta-item extension-id item-namespace :error-mode?])]
          [error-body extension-id item-namespace]
          (if @(a/subscribe [:item-editor/body-did-mount? extension-id item-namespace])
               (if-let [data-received? @(a/subscribe [:item-editor/get-meta-item extension-id item-namespace :data-received?])]
                       [form-element     extension-id item-namespace]
                       [downloading-item extension-id item-namespace]))))

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :form-element (metamorphic-content)
  ;   :handler-key (keyword)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate, :save]
  ;   :item-id (string)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :new-item-id (string)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)
  ;   :suggestions-path (vector)(opt)
  ;    Default: core.helpers/default-suggestions-path}
  ;
  ; @usage
  ;  [item-editor/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-form-element [extension-id item-namespace] [:div ...])
  ;  [item-editor/body :my-extension :my-type {:form-element #'my-form-element}]
  [extension-id item-namespace body-props]
  (let [body-props (core.prototypes/body-props-prototype extension-id item-namespace body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :body)
                           {:reagent-render         (fn []             [body-structure                 extension-id item-namespace])
                            :component-did-mount    (fn [] (a/dispatch [:item-editor/body-did-mount    extension-id item-namespace body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-editor/body-will-unmount extension-id item-namespace]))})))
