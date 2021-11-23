
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.views
    (:require [x.app-core.api     :as a]
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

(defn cancel-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  (let [parent-uri (engine/parent-uri extension-id item-namespace)]
       [elements/button ::cancel-item-button
                        {:tooltip :cancel! :preset :cancel-icon-button
                         :on-click [:x.app-router/go-to! parent-uri]}]))

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/button ::delete-item-button
                   {:tooltip :delete! :preset :delete-icon-button
                    :on-click [:item-editor/delete-item! extension-id item-namespace]}])

(defn archive-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/button ::archive-item-button
                   {:tooltip :archive! :preset :archive-icon-button
                    :on-click [:item-editor/archive-item! extension-id item-namespace]}])

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (component)
  [extension-id item-namespace]
  [elements/button ::copy-item-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button
                    :on-click [:item-editor/duplicate-item! extension-id item-namespace]}])

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:form-completed? (boolean)}
  ;
  ; @return (component)
  [extension-id item-namespace {:keys [form-completed? new-item?]}]
  [elements/button ::save-item-button
                   {:tooltip :save! :preset :save-icon-button :disabled? (not form-completed?)
                    :on-click (if new-item? [:item-editor/add-item!    extension-id item-namespace]
                                            [:item-editor/update-item! extension-id item-namespace])}])
