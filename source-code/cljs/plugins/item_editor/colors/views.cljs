
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.colors.views
    (:require [plugins.item-editor.colors.config :as colors.config]
              [mid-fruits.vector                 :as vector]
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
                        {:disabled? editor-disabled?
                         :font-size :xs
                         :indent    {:horizontal :xxs}
                         :label     :add-color!
                         :on-click  [:item-editor/render-color-picker-dialog! editor-id]
                         :preset    :muted}]))

(defn selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        current-item     @(a/subscribe [:item-editor/get-current-item editor-id])]
       [elements/color-stamp ::selected-colors-button
                             {:colors    (:colors current-item)
                              :disabled? editor-disabled?
                              :on-click  [:item-editor/render-color-picker-dialog! editor-id]
                              :size      :xxl}]))

(defn color-selector
  ; @param (keyword) editor-id
  ;
  ; @usage
  ;  [item-editor/color-selector :my-editor]
  [editor-id]
  (let [current-item @(a/subscribe [:item-editor/get-current-item editor-id])]
       [elements/row ::color-selector
                     {:content (if (-> current-item :colors vector/nonempty?)
                                   [selected-colors-button editor-id]
                                   [add-colors-button      editor-id])
                      :horizontal-align :center}]))



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
                              {:indent          {:vertical :xs}
                               :initial-options colors.config/COLORS
                               :value-path      value-path}]))
