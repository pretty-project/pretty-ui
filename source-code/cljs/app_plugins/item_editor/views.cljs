
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.8
; Compatibility: x4.5.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.views
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-layouts.api  :as layouts]
              [app-plugins.item-editor.engine :as engine]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :error-mode? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-editor/delete-item-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled? error-mode?]}]
  [elements/button ::delete-item-button
                   {:tooltip :delete! :preset :delete-icon-button
                    :disabled? (or disabled? error-mode?)
                    :on-click  [:item-editor/delete-item! extension-id item-namespace]}])

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :error-mode? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-editor/copy-item-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled? error-mode?]}]
  [elements/button ::copy-item-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button
                    :disabled? (or disabled? error-mode?)
                    :on-click  [:item-editor/duplicate-item! extension-id item-namespace]}])

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:disabled? (boolean)(opt)
  ;   :error-mode? (boolean)(opt)
  ;   :form-completed? (boolean)}
  ;
  ; @usage
  ;  [item-editor/save-item-button :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled? error-mode? form-completed?]}]
  [elements/button ::save-item-button
                   {:tooltip :save! :preset :save-icon-button
                    :disabled? (or (not form-completed?) disabled? error-mode?)
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
  ; @usage
  ;  [item-editor/item-label :my-extension :my-type {...}]
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
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled?]}]
  [elements/button ::add-colors-button
                   {:label :add-color! :preset :muted-button :layout :row :font-size :xs
                    :disabled? disabled?
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
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled?] :as element-props}]
  [elements/toggle ::selected-colors-button
                   {:disabled? disabled?
                    :on-click  [:item-editor/render-color-picker-dialog! extension-id item-namespace]
                    :content   [selected-colors extension-id item-namespace element-props]}])

(defn color-selector
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:colors (strings in vector)(opt)}
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
                              [add-colors-button      extension-id item-namespace element-props])}])

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
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [item-editor/color-stamp :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [disabled?]}]
  [elements/multiline-field ::description-field
                            {:value-path [extension-id :item-editor/data-item :description]
                             :disabled?  disabled?}])



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; @param (keyword) body-id
  ;
  ; @usage
  ;  [item-editor/error-body :my-body]
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
  (if-not new-item? [:<> [delete-item-button extension-id item-namespace header-props]
                         [copy-item-button   extension-id item-namespace header-props]]))

(defn header-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (component)
  [extension-id item-namespace header-props]
  [save-item-button extension-id item-namespace header-props])

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
  ; @usage
  ;  [item-editor/header :my-extension :my-type {...}]
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
  ; @usage
  ;  [item-editor/view :my-extension :my-type {...}]
  ;
  ; @return (component)
  [extension-id item-namespace view-props]
  (let [subscribed-props (a/subscribe [:item-editor/get-view-props extension-id item-namespace])]
       (fn [] [layout extension-id item-namespace (merge view-props @subscribed-props)])))
