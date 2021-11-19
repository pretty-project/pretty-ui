
(ns plugins.item-editor.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-button
  ; @param (string) extension-name
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::undo-delete-button
                   {:label :undo-delete! :variant :transparent :horizontal-align :left :color :warning
                    :on-click [:item-editor/undo-last-deleted! extension-name]}])

(defn edit-copy-button
  ; @param (string) extension-name
  ;
  ; @return (component)
  [extension-name]
  [elements/button ::edit-copy-button
                   {:label :edit-copy! :variant :transparent :horizontal-align :left :color :primary
                    :on-click [:item-editor/edit-last-duplicated! extension-name]}])

(defn cancel-item-button
  ; @param (string) extension-name
  ;
  ; @return (component)
  [extension-name]
  (let [parent-path (str "/" extension-name)]
       [elements/button ::cancel-item-button
                        {:tooltip :cancel! :preset :cancel-icon-button
                         :on-click [:x.app-router/go-to! parent-path]}]))

(defn delete-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (component)
  [extension-name item-name]
  [elements/button ::delete-item-button
                   {:tooltip :delete! :preset :delete-icon-button
                    :on-click [:item-editor/delete-item! extension-name item-name]}])

(defn archive-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (component)
  [extension-name item-name]
  [elements/button ::archive-item-button
                   {:tooltip :archive! :preset :archive-icon-button
                    :on-click [:item-editor/archive-item! extension-name item-name]}])

(defn copy-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (component)
  [extension-name item-name]
  [elements/button ::copy-item-button
                   {:tooltip :duplicate! :preset :duplicate-icon-button
                    :on-click [:item-editor/duplicate-item! extension-name item-name]}])

(defn save-item-button
  ; @param (string) extension-name
  ; @param (string) item-name
  ; @param (map) header-props
  ;  {:form-completed? (boolean)}
  ;
  ; @return (component)
  [extension-name item-name {:keys [form-completed? new-item?]}]
  [elements/button ::save-item-button
                   {:tooltip :save! :preset :save-icon-button :disabled? (not form-completed?)
                    :on-click (if new-item? [:item-editor/add-item!    extension-name item-name]
                                            [:item-editor/update-item! extension-name item-name])}])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------
