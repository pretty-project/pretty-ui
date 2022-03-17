
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.colors.views
    (:require [mid-fruits.vector                 :as vector]
              [plugins.item-editor.colors.config :as colors.config]
              [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]))



;; -- Color-selector components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
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
  [extension-id item-namespace]
  (letfn [(f [selected-colors color] (conj selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))]
         (let [current-item @(a/subscribe [:item-editor/get-current-item extension-id item-namespace])]
              (reduce f [:div.item-editor--selected-colors] (:colors current-item)))))

(defn selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/toggle ::selected-colors-button
                        {:disabled? editor-disabled?
                         :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                         :content   [selected-colors                          extension-id item-namespace]}]))

(defn color-selector
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/color-selector :my-extension :my-type]
  [extension-id item-namespace]
  (let [current-item @(a/subscribe [:item-editor/get-current-item extension-id item-namespace])]
       [elements/row ::color-selector
                     {:horizontal-align :center
                      :content (if (-> current-item :colors vector/nonempty?)
                                   [selected-colors-button extension-id item-namespace]
                                   [add-colors-button      extension-id item-namespace])}]))



;; -- Color-stamp components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-stamp
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @usage
  ;  [item-editor/color-stamp :my-extension :my-type {...}]
  [_ _ {:keys [colors]}]
  (if (vector/nonempty? colors)
      (letfn [(f [color-stamp color] (conj color-stamp [:div.item-editor--color-stamp--color {:style {:background-color color}}]))]
             (reduce f [:div.item-editor--color-stamp] colors))
      [:div.item-editor--color-stamp-placeholder]))



;; -- Color-picker components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [item-path @(a/subscribe [:item-editor/get-body-prop extension-id item-namespace :item-path])
        value-path (conj item-path :colors)]
       [elements/color-picker ::color-picker
                              {:initial-options colors.config/COLORS :value-path value-path}]))
