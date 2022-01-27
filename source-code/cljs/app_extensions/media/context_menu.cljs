
(ns app-extensions.media.context-menu
    (:require [mid-fruits.candy        :refer [param]]
              [x.app-elements.api      :as elements]
              [app-extensions.media.engine :as engine]))



;; -- Context-menu common components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn context-menu-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [label]}]
  [elements/label {:color            :highlight
                   :content          label
                   :font-size        :xs
                   :font-weight      :normal
                   :horizontal-align :center
                   :layout           :fit}])

(defn context-menu-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (component)
  [element-id header-props]
  [:<> [context-menu-label element-id header-props]
       [elements/horizontal-separator {:size :xxs}]
       [elements/horizontal-line      {:color :highlight}]
       [elements/horizontal-separator {:size :xxs}]])

(defn context-menu-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) button-props
  ;  {:on-click (metamorphic-event)}
  ;
  ; @return (component)
  [element-id {:keys [on-click] :as button-props}]
  ; A context-menu felületeken megjelenő gombokra kattintva a context-menu felület bezáródik.
  (let [on-click {:dispatch-n [on-click [:elements/close-context-surface! element-id]]}]
       [elements/button (merge {:horizontal-align :left
                                :layout           :fit
                                :preset           :default-button}
                               (param button-props)
                               {:on-click  on-click})]))



;; -- File-storage context-menu components ------------------------------------
;; ----------------------------------------------------------------------------

(defn file-storage-subdirectory-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-content)}
  ;
  ; @return (component)
  [element-id {:directory/keys [alias]}]
  (let [subdirectory-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Open button
            [context-menu-button
              element-id
              {:label    :open!
               :on-click [:file-storage/go-to! subdirectory-id]}]
            ; Rename button
            [context-menu-button
              element-id
              {:label    :rename!
               :on-click [:file-storage/edit-rendered-subdirectory-alias! subdirectory-id]}]
            ; Copy button
            [context-menu-button
              element-id
              {:label    :copy!
               :on-click [:file-storage/render-copy-rendered-subdirectory-dialog! subdirectory-id]}]
            ; Move button
            [context-menu-button
              element-id
              {:label    :move!
               :on-click [:file-storage/render-move-rendered-subdirectory-dialog! subdirectory-id]}]
            ; Delete button
            [context-menu-button
              element-id
              {:label    :delete!
               :on-click [:file-storage/render-delete-rendered-subdirectory-dialog! subdirectory-id]}]
            ; Properties button
            [context-menu-button
              element-id
              {:label    :properties
               :on-click [:file-storage/render-rendered-subdirectory-properties! subdirectory-id]}]]))

(defn file-storage-file-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-content)
  ;   :file/filename (string)}
  ;
  ; @return (component)
  [element-id {:file/keys [alias filename]}]
  (let [file-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Rename button
            [context-menu-button
              element-id
              {:label    :rename!
               :on-click [:file-storage/edit-rendered-file-alias! file-id]}]
            ; Copy button
            [context-menu-button
              element-id
              {:label    :copy!
               :on-click [:file-storage/render-copy-rendered-file-dialog! file-id]}]
            ; Move button
            [context-menu-button
              element-id
              {:label    :move!
               :on-click [:file-storage/render-move-rendered-file-dialog! file-id]}]
            ; Download button
            [context-menu-button
              element-id
              {:label    :download!
               :on-click [:file-storage/save-rendered-file! file-id]}]
            ; Delete button
            [context-menu-button
              element-id
              {:label    :delete!
               :on-click [:file-storage/render-delete-rendered-file-dialog! file-id]}]
            ; Copy link button
            [context-menu-button
              element-id
              {:label :copy-link!
               :on-click [:media-storage/copy-file-link! filename]}]
            ; Properties button
            [context-menu-button
              element-id
              {:label    :properties
               :on-click [:file-storage/render-rendered-file-properties! file-id]}]]))



;; -- File-uploader context-menu components -----------------------------------
;; ----------------------------------------------------------------------------

(defn file-uploader-file-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) file-props
  ;  {:filename (string)}
  ;
  ; @return (component)
  [element-id {:keys [filename]}]
  (let [file-dex (engine/element-id->file-dex element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label filename}]
            ; Remove button
            [context-menu-button
              element-id
              {:label    :remove!
               :on-click [:file-uploader/abort-file! file-dex]}]]))



;; -- File-browser context-menu components ------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selector-subdirectory-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-content)}
  ;
  ; @return (component)
  [element-id {:directory/keys [alias]}]
  (let [subdirectory-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Open button
            [context-menu-button
              element-id
              {:label    :open!
               :on-click [:file-browser/go-to! subdirectory-id]}]]))

(defn file-selector-file-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-content)
  ;   :file/filename (string)}
  ;
  ; @return (component)
  [element-id {:file/keys [alias filename]}]
  (let [file-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Select button
;           [context-menu-button
;             element-id
;             {:label    (if selected? :unselect :select)
;              :on-click [:file-browser/toggle-file-selection! file-id]}]
            ; Copy link button
            [context-menu-button
              element-id
              {:label :copy-link!
               :on-click [:media/copy-file-link! filename]}]
            ; Properties button
            [context-menu-button
              element-id
              {:label    :properties
               :on-click []
               :disabled? true}]]))

(defn directory-selector-subdirectory-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) subdirectory-props
  ;  {:directory/alias (metamorphic-content)}
  ;
  ; @return (component)
  [element-id {:directory/keys [alias]}]
  (let [subdirectory-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Open button
            [context-menu-button
              element-id
              {:label    :open!
               :on-click [:file-browser/go-to! subdirectory-id]}]]))

(defn directory-selector-file-context-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) file-props
  ;  {:file/alias (metamorphic-content)
  ;   :file/filename (string)}
  ;
  ; @return (component)
  [element-id {:file/keys [alias filename selected?]}]
  (let [file-id (engine/element-id->item-id element-id)]
            ; Menu header
       [:<> [context-menu-header
              element-id
              {:label alias}]
            ; Copy link button
            [context-menu-button
              element-id
              {:label :copy-link!
               :on-click [:media/copy-file-link! filename]}]
            ; Properties button
            [context-menu-button
              element-id
              {:label    :properties
               :on-click []
               :disabled? true}]]))
