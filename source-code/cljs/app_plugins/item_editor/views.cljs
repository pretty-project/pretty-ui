
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
    (:require [mid-fruits.candy     :refer [param]]
              [mid-fruits.string    :as string]
              [mid-fruits.vector    :as vector]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]
              [x.app-layouts.api    :as layouts]
              [app-plugins.item-editor.engine :as engine]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:error-mode? (boolean)(opt)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-mode? synchronizing?]}]
  [elements/button ::delete-item-button
                   {:tooltip :delete! :preset :delete-icon-button
                    :disabled? (or error-mode? synchronizing?)
                    :on-click  [:item-editor/delete-item! extension-id item-namespace]}])

(defn archive-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:archived? (boolean)(opt)
  ;   :error-mode? (boolean)(opt)
  ;   :handle-archived-items? (boolean)
  ;   :synchronizing? (boolean)(opt)}

  ;
  ; @return (component)
  [extension-id item-namespace {:keys [archived? error-mode? handle-archived-items? synchronizing?]}]
  (cond (and handle-archived-items? archived?)
        [elements/button ::archive-item-button
                         {:tooltip :unarchive! :preset :archived-icon-button
                          :disabled? (or error-mode? synchronizing?)
                          :on-click  [:item-editor/mark-item! extension-id item-namespace
                                       {:marker-key :archived? :toggle-f not :marked-message :item-unarchived}]}]
        (and handle-archived-items? (not archived?))
        [elements/button ::archive-item-button
                         {:tooltip :archive! :preset :archive-icon-button
                          :disabled? (or error-mode? synchronizing?)
                          :on-click  [:item-editor/mark-item! extension-id item-namespace
                                       {:marker-key :archived? :toggle-f not :marked-message :item-archived}]}]))

(defn favorite-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:favorite? (boolean)(opt)
  ;   :error-mode? (boolean)(opt)
  ;   :handle-favorite-items? (boolean)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-mode? favorite? handle-favorite-items? synchronizing?]}]
  (cond (and handle-favorite-items? favorite?)
        [elements/button ::favorite-item-button
                         ; Az :added-to-favorites tooltip túlságosan széles, ezért a favorite-item-button
                         ; elemen a tooltip feliratok ki vannak kapcsolva
                         {:preset :added-to-favorites-icon-button ;:tooltip :added-to-favorites
                          :disabled? (or error-mode? synchronizing?)
                          :on-click  [:item-editor/mark-item! extension-id item-namespace
                                       {:marker-key :favorite? :toggle-f not :marked-message :removed-from-favorites}]}]
        (and handle-favorite-items? (not favorite?))
        [elements/button ::favorite-item-button
                         {:preset :add-to-favorites-icon-button ;:tooltip :add-to-favorites!
                          :disabled? (or error-mode? synchronizing?)
                          :on-click  [:item-editor/mark-item! extension-id item-namespace
                                       {:marker-key :favorite? :toggle-f not :marked-message :added-to-favorites}]}]))

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:error-mode? (boolean)(opt)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-mode? synchronizing?]}]
  [elements/button ::copy-item-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button
                    :disabled? (or error-mode? synchronizing?)
                    :on-click  [:item-editor/duplicate-item! extension-id item-namespace]}])

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:error-mode? (boolean)(opt)
  ;   :form-completed? (boolean)
  ;   :synchronizing? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [error-mode? form-completed? synchronizing?]}]
  [elements/button ::save-item-button
                   {:tooltip :save! :preset :save-icon-button
                    :disabled? (or (not form-completed?) error-mode? synchronizing?)
                    :on-click [:item-editor/save-item! extension-id item-namespace]}])



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
  ;  {:item-name (metamorphic-content)}
  ;
  ; @return (component)
  [_ _ {:keys [item-name]}]
  [elements/label ::named-item-label
                  {:content item-name :font-weight :extra-bold :font-size :l}])

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
  ;  {:item-name (metamorphic-content)(opt)
  ;   :new-item? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [item-name new-item?] :as element-props}]
  (cond (string/nonempty? item-name) [named-item-label   extension-id item-namespace element-props]
        (boolean          new-item?) [new-item-label     extension-id item-namespace element-props]
        :unnamed-item                [unnamed-item-label extension-id item-namespace element-props]))



;; -- Color-selector components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-colors-button
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
                    :disabled? synchronizing?
                    :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]}])


(defn selected-colors
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
              (conj selected-colors [:div.item-editor--selected-color {:style {:background-color color}}]))
          [:div.item-editor--selected-colors]
          (param colors)))

(defn selected-colors-button
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
                   {:disabled? synchronizing?
                    :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                    :content   [selected-colors extension-id item-namespace element-props]}])

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
  ; @return (component)
  [extension-id item-namespace]
  [elements/multiline-field ::description-field
                            {:value-path [extension-id :item-editor/data-item :description]}])



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; @param (keyword) body-id
  ;
  ; @return (component)
  [body-id]
  [:<> [elements/label {:content :an-error-occured :font-size :m :layout :fit}]
       [elements/horizontal-separator {:size :xs}]
       [elements/label {:content :the-item-you-opened-may-be-broken :color :muted :layout :fit}]
       [elements/horizontal-separator {:size :xs}]])



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [new-item?] :as header-props}]
  (if-not new-item? [:<> [delete-item-button  extension-id item-namespace header-props]
                         [copy-item-button    extension-id item-namespace header-props]
                         [archive-item-button extension-id item-namespace header-props]]))

(defn header-end-buttons
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

(defn header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:new-item? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [elements/horizontal-polarity {:start-content [header-start-buttons extension-id item-namespace header-props]
                                 :end-content   [header-end-buttons   extension-id item-namespace header-props]}])

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) header-props
  ;
  ; @return (component)
  ([extension-id item-namespace]
   [header extension-id item-namespace {}])

  ([extension-id item-namespace header-props]
   (let [subscribed-props (a/subscribe [:item-editor/get-header-props extension-id item-namespace])]
        (fn [] [header-structure extension-id item-namespace (merge header-props @subscribed-props)]))))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:description (metamorphic-content)(opt)
  ;   :error-mode? (boolean)(opt)
  ;   :form-element (component)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [description error-mode? form-element]}]
  (if error-mode? ; If error-mode is enabled ...
                  [layouts/layout-a extension-id {:body   {:content [error-body]}
                                                  :header {:content [header extension-id item-namespace]}}]
                  ; If error-mode is NOT enabled ...
                  [layouts/layout-a extension-id {:description description
                                                  :body   {:content [form-element]}
                                                  :header {:content [header extension-id item-namespace]}}]))

(defn view
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) view-props
  ;  {:form-element (component)}
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-editor/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
