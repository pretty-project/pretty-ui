
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
  ; @param (map) element-props
  ;  {:colors (strings in vector)}
  [extension-id item-namespace {:keys [colors]}]
  (letfn [(f [selected-colors color] (conj selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))]
         (reduce f [:div.item-editor--selected-colors] colors)))

(defn selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
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
  [extension-id _]
  [elements/color-picker ::color-picker
                         {:initial-options colors.config/COLORS
                          :value-path [extension-id :item-editor/data-items :colors]}])
