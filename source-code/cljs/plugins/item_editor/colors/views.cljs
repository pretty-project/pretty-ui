
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
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])]
       [elements/button ::add-colors-button
                        {:label :add-color! :preset :muted-button :layout :row :font-size :xs
                         :disabled? editor-disabled?
                         :on-click  [:item-editor/render-color-picker-dialog! editor-id]}]))

(defn selected-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (letfn [(f [selected-colors color] (conj selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))]
         (let [current-item @(a/subscribe [:item-editor/get-current-item editor-id])]
              (reduce f [:div.item-editor--selected-colors] (:colors current-item)))))

(defn selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])]
       [elements/toggle ::selected-colors-button
                        {:disabled? editor-disabled?
                         :on-click  [:item-editor/render-color-picker-dialog! editor-id]
                         :content   [selected-colors                          editor-id]}]))

(defn color-selector
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/color-selector :my-editor]
  [editor-id]
  (let [current-item @(a/subscribe [:item-editor/get-current-item editor-id])]
       [elements/row ::color-selector
                     {:horizontal-align :center
                      :content (if (-> current-item :colors vector/nonempty?)
                                   [selected-colors-button editor-id]
                                   [add-colors-button      editor-id])}]))



;; -- Color-stamp components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-stamp
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @usage
  ;  [item-editor/color-stamp :my-editor {...}]
  [_ {:keys [colors]}]
  (if (vector/nonempty? colors)
      (letfn [(f [color-stamp color] (conj color-stamp [:div.item-editor--color-stamp--color {:style {:background-color color}}]))]
             (reduce f [:div.item-editor--color-stamp] colors))
      [:div.item-editor--color-stamp-placeholder]))



;; -- Color-picker components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-dialog-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-path @(a/subscribe [:item-editor/get-body-prop editor-id :item-path])
        value-path (conj item-path :colors)]
       [elements/color-picker ::color-picker
                              {:initial-options colors.config/COLORS :value-path value-path}]))
