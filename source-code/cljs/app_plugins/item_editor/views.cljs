
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.7.8
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.views
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [app-plugins.item-editor.engine :as engine]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/delete-item-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/button ::delete-item-button
                        {:tooltip :delete! :preset :delete-icon-button
                         :disabled? editor-disabled?
                         :on-click  [:item-editor/delete-item! extension-id item-namespace]}]))

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/copy-item-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/button ::copy-item-button
                        {:tooltip :duplicate! :preset :duplicate-icon-button
                         :disabled? editor-disabled?
                         :on-click  [:item-editor/duplicate-item! extension-id item-namespace]}]))

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/save-item-button :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  extension-id item-namespace])]
       [elements/button ::save-item-button
                        {:tooltip :save! :preset :save-icon-button
                         :disabled? (or (not form-completed?) editor-disabled?)
                         :on-click [:item-editor/save-item! extension-id item-namespace]}]))



;; -- Form components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [new-item-label (engine/new-item-label extension-id item-namespace)]
       [elements/label ::new-item-label
                       {:content new-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  ;
  ; @return (component)
  [_ _ {:keys [name]}]
  [elements/label ::named-item-label
                  {:content name :font-weight :extra-bold :font-size :l}])

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [unnamed-item-label (engine/unnamed-item-label extension-id item-namespace)]
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
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [name] :as element-props}]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       (cond (string/nonempty? name)      [named-item-label   extension-id item-namespace element-props]
             (boolean          new-item?) [new-item-label     extension-id item-namespace]
             :unnamed-item                [unnamed-item-label extension-id item-namespace])))



;; -- Color-selector components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/button ::add-colors-button
                        {:label :add-color! :preset :muted-button :layout :row :font-size :xs
                         :disabled? editor-disabled?
                         :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]}]))

(defn selected-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [colors]}]
  (reduce (fn [selected-colors color]
              (conj selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))
          [:div.item-editor--selected-colors]
          (param colors)))

(defn selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;
  ; @return (component)
  [extension-id item-namespace element-props]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/toggle ::selected-colors-button
                        {:disabled? editor-disabled?
                         :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                         :content   [selected-colors                          extension-id item-namespace element-props]}]))

(defn color-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)}
  ;
  ; @usage
  ;  [item-editor/color-selector :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [colors] :as element-props}]
  [elements/row ::color-selector
                {:horizontal-align :center
                 :content (if (vector/nonempty? colors)
                              [selected-colors-button extension-id item-namespace element-props]
                              [add-colors-button      extension-id item-namespace])}])

(defn color-stamp
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @usage
  ;  [item-editor/color-stamp :my-extension :my-type {...}]
  ;
  ; @return (component)
  [_ _ {:keys [colors]}]
  (if (vector/nonempty? colors)
      (reduce (fn [color-stamp color]
                  (conj color-stamp [:div.item-editor--color-stamp--color {:style {:background-color color}}]))
              [:div.item-editor--color-stamp]
              (param colors))
      [:div.item-editor--color-stamp-placeholder]))



;; -- Input components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn description-field
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/color-stamp :my-extension :my-type]
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/multiline-field ::description-field
                                 {:value-path [extension-id :item-editor/data-items :description]
                                  :disabled?  editor-disabled?}]))



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/error-body :my-extension :my-type]
  ;
  ; @return (component)
  [_ _]
  [:<> [elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-item-you-opened-may-be-broken :color :muted}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       (if-not new-item? [:<> [delete-item-button extension-id item-namespace]
                              [copy-item-button   extension-id item-namespace]])))

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [save-item-button extension-id item-namespace])

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/horizontal-polarity {:start-content [menu-start-buttons extension-id item-namespace]
                                 :end-content   [menu-end-buttons   extension-id item-namespace]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:menu (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/header :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-menu [] [:div ...])
  ;  [item-editor/header :my-extension :my-type {:menu #'my-menu}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [menu]}]
  (if menu [menu]
           [menu-mode-header extension-id item-namespace]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:form-element (metamorphic-content)}
  ;
  ; @usage
  ;  [item-editor/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] [:div ...])
  ;  [item-editor/body :my-extension :my-type {:form-element #'my-form-element}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [form-element]}]
  [components/stated {:render-f   form-element
                      :destructor [:item-editor/unload-editor! extension-id item-namespace]}])



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:form-element (metamorphic-content)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate]
  ;    TODO ...
  ;   :menu (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/view :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] [:div ...])
  ;  (defn my-menu         [] [:div ...])
  ;  [item-editor/view :my-extension :my-type {:form-element #'my-form-element
  ;                                            :menu         #'my-menu}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [description @(a/subscribe [:item-editor/get-description extension-id item-namespace])]
       (if-let [error-mode? @(a/subscribe [:item-editor/error-mode?     extension-id item-namespace])]
               [layouts/layout-a extension-id {:body   [error-body extension-id item-namespace]
                                               :header [header     extension-id item-namespace view-props]}]
               [layouts/layout-a extension-id {:description description
                                               :body   [body   extension-id item-namespace view-props]
                                               :header [header extension-id item-namespace view-props]}])))
