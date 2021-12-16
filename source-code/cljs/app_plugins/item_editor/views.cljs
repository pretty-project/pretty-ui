
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.8
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.views
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-plugins.item-editor.engine :as engine]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-button
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::undo-delete-button
                   {:label :undo-delete! :variant :transparent :horizontal-align :left :color :warning
                    :on-click [:item-editor/undo-last-deleted! extension-id]}])

(defn edit-copy-button
  ; @param (keyword) extension-id
  ;
  ; @return (component)
  [extension-id]
  [elements/button ::edit-copy-button
                   {:label :edit-copy! :variant :transparent :horizontal-align :left :color :primary
                    :on-click [:item-editor/edit-last-duplicated! extension-id]}])

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:error-occured? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-occured?]}]
  [elements/button ::delete-item-button
                   {:tooltip :delete! :preset :delete-icon-button :disabled? error-occured?
                    :on-click [:item-editor/delete-item! extension-id item-namespace]}])

(defn archive-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:archived? (boolean)(opt)
  ;   :error-occured? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [archived? error-occured?]}]
  [elements/button ::archive-item-button
                   (if archived? {:tooltip :archived :preset :archived-icon-button :disabled? error-occured?
                                  :on-click [:item-editor/unmark-item! extension-id item-namespace
                                                                       {:marker-key       :archived?
                                                                        :unmarked-message :archived-item-restored}]}
                                 {:tooltip :archive! :preset :archive-icon-button :disabled? error-occured?
                                  :on-click [:item-editor/mark-item! extension-id item-namespace
                                                                     {:marker-key     :archived?
                                                                      :marked-message :archived}]})])

(defn favorite-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:favorite? (boolean)(opt)
  ;   :error-occured? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-occured? favorite?]}]
  [elements/button ::favorite-item-button
                   ; Az :added-to-favorites tooltip túlságosan széles, ezért a favorite-item-button
                   ; elemen a tooltip feliratok ki vannak kapcsolva
                   (if favorite? {:preset :added-to-favorites-icon-button :disabled? error-occured? ; :tooltip :added-to-favorites
                                  :on-click [:item-editor/unmark-item! extension-id item-namespace
                                                                       {:marker-key       :favorite?
                                                                        :unmarked-message :removed-from-favorites}]}
                                 {:preset :add-to-favorites-icon-button :disabled? error-occured? ; :tooltip :add-to-favorites!
                                  :on-click [:item-editor/mark-item! extension-id item-namespace
                                                                     {:marker-key       :favorite?
                                                                      :marked-message :added-to-favorites}]})])

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:error-occured? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-occured?]}]
  [elements/button ::copy-item-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button :disabled? error-occured?
                    :on-click [:item-editor/duplicate-item! extension-id item-namespace]}])

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:error-occured? (boolean)(opt)
  ;   :form-completed? (boolean)
  ;   :new-item? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-occured? form-completed? new-item?]}]
  [elements/button ::save-item-button
                   {:tooltip :save! :preset :save-icon-button :disabled? (or (not form-completed?) error-occured?)
                    :on-click (if new-item? [:item-editor/add-item!  extension-id item-namespace]
                                            [:item-editor/save-item! extension-id item-namespace])}])



;; -- Form components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-label
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [_ _ {:keys [content]}]
  [elements/label {:content     content
                   :font-size   :m
                   :font-weight :extra-bold
                   :indent      :none
                   :layout      :fit}])

(defn input-group-footer
  ; @return (component)
  []
  [elements/separator {:orientation :horizontal :size :xxl}])

(defn form-footer
  ; @return (component)
  []
  [elements/separator {:orientation :horizontal :size :l}])

(defn form-header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:form-label (string)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [form-label] :as element-props}]
  [elements/row ::form-header
                {:horizontal-align :center
                 :content [elements/label {:content     form-label
                                           :font-size   :m
                                           :font-weight :extra-bold
                                           :layout      :fit}]}])



;; -- Color-selector components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [synchronizing?]}]
  [elements/button ::add-colors-button
                   {:label :add-color! :preset :muted-button :layout :row :font-size :xs
                    :on-click [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                    :disabled? synchronizing?}])

(defn- selected-colors
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @return (component)
  [_ _ {:keys [colors]}]
  (reduce (fn [selected-colors color]
              (vector/conj-item selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))
          [:div.item-editor--selected-colors]
          (param colors)))

(defn- selected-colors-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [synchronizing?] :as element-props}]
  [elements/toggle ::selected-colors-button
                   {:on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                    :content   [selected-colors extension-id item-namespace element-props]
                    :disabled? synchronizing?}])

(defn color-selector
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [colors] :as element-props}]
  [elements/row ::color-selector
                {:horizontal-align :center
                 :content (if (vector/nonempty? colors)
                              [selected-colors-button extension-id item-namespace element-props]
                              [add-colors-button      extension-id item-namespace element-props])}])

(defn color-stamp
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
  ;
  ; @return (component)
  [_ _ {:keys [colors]}]
  (if (vector/nonempty? colors)
      (reduce (fn [color-stamp color]
                  (vector/conj-item color-stamp [:div.item-editor--color-stamp--color {:style {:background-color color}}]))
              [:div.item-editor--color-stamp]
              (param colors))
      [:div.item-editor--color-stamp-placeholder]))



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; @param (keyword) body-id
  ;
  ; @return (component)
  [body-id]
  [:<> [elements/label     {:content :an-error-occured :font-size :m :layout :fit}]
       [elements/separator {:orientation :horizontal :size :xs}]
       [elements/label     {:content :the-item-you-opened-may-be-broken :color :muted :layout :fit}]
       [elements/separator {:orientation :horizontal :size :xs}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [:<> [delete-item-button  extension-id item-namespace header-props]
       [copy-item-button    extension-id item-namespace header-props]
       [archive-item-button extension-id item-namespace header-props]])

(defn- header-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [new-item?] :as header-props}]
  [:<> (if-not new-item? [favorite-item-button extension-id item-namespace header-props])
       [save-item-button extension-id item-namespace header-props]])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [new-item?] :as header-props}]
  [elements/polarity {:start-content (if-not new-item? [header-start-buttons extension-id item-namespace header-props])
                      :end-content   [header-end-buttons extension-id item-namespace header-props]}])
