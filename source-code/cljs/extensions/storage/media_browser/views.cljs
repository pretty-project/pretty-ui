
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.views
    (:require [extensions.storage.core.config           :as core.config]
              [extensions.storage.media-browser.helpers :as media-browser.helpers]
              [mid-fruits.io                            :as io]
              [plugins.item-browser.api                 :as item-browser]
              [x.app-components.api                     :as components]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.api                       :as elements]
              [x.app-layouts.api                        :as layouts]
              [x.app-ui.api                             :as ui]))



;; -- Item-menu components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-menu-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias] :as media-item}]
  [elements/label ::media-menu-label
                  {:color     :muted
                   :content   alias
                   :font-size :xs
                   :indent    {:horizontal :xxs :left :s}}])

(defn media-menu-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item]
  [elements/horizontal-polarity ::media-menu-header
                                {:start-content [media-menu-label media-item]}])
                                 ;:end-content   [ui/popup-close-icon-button :storage.media-browser/media-menu {}]}])



;; -- Directory-item menu components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-item]
  [:<> [elements/button ::open-directory-button
                        {:hover-color :highlight
                         :icon        :folder
                         :icon-family :material-icons-outlined
                         :indent      {:vertical :xs}
                         :label       :open!
                         :on-click    [:storage.media-browser/open-directory! directory-item]
                         :preset      :default}]
       [elements/button ::copy-directory-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-directory-link! directory-item]
                         :preset      :default}]
       [elements/button ::move-directory-button
                        {:disabled?   true
                         :hover-color :highlight
                         :icon        :drive_file_move
                         :icon-family :material-icons-outlined
                         :indent      {:vertical :xs}
                         :label       :move!
                         :on-click    [:storage.media-browser/move-item! directory-item]
                         :preset      :default}]
       [elements/button ::duplicate-directory-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! directory-item]
                         :preset      :default}]
       [elements/button ::rename-directory-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! directory-item]
                         :preset      :default}]
       [elements/button ::delete-directory-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! directory-item]
                         :preset      :warning}]])



;; -- File-item menu components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [mime-type] :as file-item}]
  [:<> (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:hover-color :highlight
                             :icon :preview
                             :indent {:vertical :xs}
                             :label :file-preview
                             :on-click [:storage.media-browser/preview-file! file-item]
                             :preset :default}])
       [elements/button ::download-file-button
                        {:hover-color :highlight
                         :icon        :cloud_download
                         :indent      {:vertical :xs}
                         :label       :download!
                         :on-click    [:storage.media-browser/download-file! file-item]
                         :preset      :default}]
       [elements/button ::copy-file-link-button
                        {:hover-color :highlight
                         :icon        :content_paste
                         :indent      {:vertical :xs}
                         :label       :copy-link!
                         :on-click    [:storage.media-browser/copy-file-link! file-item]
                         :preset      :default}]
       [elements/button ::move-file-button
                        {:hover-color :highlight
                         :icon        :drive_file_move
                         :indent      {:vertical :xs}
                         :label       :move!
                         :icon-family :material-icons-outlined
                         :on-click    [:storage.media-browser/move-item! file-item]
                         :preset      :default
                         ; TEMP
                         :disabled? true}]
       [elements/button ::duplicate-file-button
                        {:hover-color :highlight
                         :icon        :content_copy
                         :indent      {:vertical :xs}
                         :label       :duplicate!
                         :on-click    [:storage.media-browser/duplicate-item! file-item]
                         :preset      :default}]
       [elements/button ::rename-file-button
                        {:hover-color :highlight
                         :icon        :edit
                         :indent      {:vertical :xs}
                         :label       :rename!
                         :on-click    [:storage.media-browser/rename-item! file-item]
                         :preset      :default}]
       [elements/button ::delete-file-button
                        {:hover-color :highlight
                         :icon        :delete_outline
                         :indent      {:vertical :xs}
                         :label       :delete!
                         :on-click    [:storage.media-browser/delete-item! file-item]
                         :preset      :warning}]])



;; -- Media-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias content-size id items modified-at] :as media-item}]
  [item-browser/list-item :storage.media-browser item-dex
                          {:icon           :navigate_next
                           :label          (str alias)
                           :description    (media-browser.helpers/directory-item->size   media-item)
                           :header         (media-browser.helpers/directory-item->header media-item)
                           :on-click       [:item-browser/browse-item! :storage.media-browser id]
                           :on-right-click [:storage.media-browser/render-directory-menu! media-item]
                           :timestamp      modified-at}])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias filename modified-at] :as media-item}]
  [item-browser/list-item :storage.media-browser item-dex
                          {:icon           :more_vert
                           :label          (str alias)
                           :description    (media-browser.helpers/file-item->size    media-item)
                           :header         (media-browser.helpers/file-item->header  media-item)
                           :on-click       [:storage.media-browser/render-file-menu! media-item]
                           :on-right-click [:storage.media-browser/render-file-menu! media-item]
                           :timestamp      modified-at}])

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item item-dex media-item]
                                      [file-item      item-dex media-item]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn storage-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          (let [label @(a/subscribe [:item-browser/get-current-item-label :storage.media-browser])]
               [:<> ;[ui/title-sensor {:title label}]
                    [elements/label ::storage-label
                                    {:content     label
                                     :font-size   :xl
                                     :font-weight :extra-bold
                                     :indent      {:top :xxl}}]])))

(defn storage-directory-content-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [data-received? @(a/subscribe [:item-browser/data-received? :storage.media-browser])]
          (let [browsed-directory @(a/subscribe [:item-browser/get-current-item :storage.media-browser])
                directory-content  (media-browser.helpers/directory-item->size browsed-directory)]
               [elements/label ::storage-directory-content-label
                               {:color       :muted
                                :content     directory-content
                                :font-size   :xxs
                                :font-weight :extra-bold}])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-browser/header :storage.media-browser
                       {:new-item-event   [:storage.media-browser/add-new-item!]
                        :new-item-options [:create-directory! :upload-files!]}])

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [:<> [storage-label]
       [storage-directory-content-label]
       [elements/horizontal-separator {:size :xxl}]
       [item-browser/body :storage.media-browser
                          {:auto-title?     true
                           :default-item-id core.config/ROOT-DIRECTORY-ID
                           :item-actions    [:delete :duplicate]
                           :item-path       [:storage :media-browser/browsed-item]
                           :items-path      [:storage :media-browser/downloaded-items]
                           :items-key       :items
                           :label-key       :alias
                           :path-key        :path
                           :list-element    #'media-item
                           :search-keys     [:alias]}]])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [layouts/layout-a ::view
                    {:body   #'body
                     :header #'header}])
