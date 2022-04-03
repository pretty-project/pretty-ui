
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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-click-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [id mime-type] :as media-item}]
  (let [on-click (case mime-type "storage/directory" [:item-browser/browse-item! :storage.media-browser id]
                                                     [:storage.media-browser/render-file-menu! media-item])]
       [:item-browser/item-clicked :storage.media-browser item-dex {:on-click on-click}]))



;; -- Item-menu components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-menu-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias] :as media-item}]
  [elements/label {:color :muted :indent :left :content alias :font-size :xs}])

(defn media-menu-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item]
  [elements/horizontal-polarity {:start-content [media-menu-label media-item]
                                 :end-content   [ui/popup-close-icon-button :storage.media-browser/media-menu {}]}])



;; -- Directory-item menu components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-item]
  [:<> [elements/button ::open-directory-button
                        {:preset :default-button :icon :folder :indent :left :label :open!
                         :icon-family :material-icons-outlined
                         :on-click [:storage.media-browser/open-directory! directory-item]}]
       [elements/button ::copy-directory-link-button
                        {:preset :default-button :icon :content_paste :indent :left :label :copy-link!
                         :on-click [:storage.media-browser/copy-directory-link! directory-item]}]
       [elements/button ::move-directory-button
                        {:preset :default-button :icon :drive_file_move :indent :left :label :move!
                         :icon-family :material-icons-outlined
                         :on-click [:storage.media-browser/move-item! directory-item]
                         :disabled? true}]
       [elements/button ::duplicate-directory-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!
                         :on-click [:storage.media-browser/duplicate-item! directory-item]}]
       [elements/button ::rename-directory-button
                        {:preset :default-button :icon :edit :indent :left :label :rename!
                         :on-click [:storage.media-browser/rename-item! directory-item]}]
       [elements/button ::delete-directory-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!
                         :on-click [:storage.media-browser/delete-item! directory-item]}]])



;; -- File-item menu components -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [mime-type] :as file-item}]
  [:<> (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:preset :default-button :icon :preview :indent :left :label :file-preview
                             :on-click [:storage.media-browser/preview-file! file-item]}])
       [elements/button ::download-file-button
                        {:preset :default-button :icon :cloud_download :indent :left :label :download!
                         :on-click [:storage.media-browser/download-file! file-item]}]
       [elements/button ::copy-file-link-button
                        {:preset :default-button :icon :content_paste :indent :left :label :copy-link!
                         :on-click [:storage.media-browser/copy-file-link! file-item]}]
       [elements/button ::move-file-button
                        {:preset :default-button :icon :drive_file_move :indent :left :label :move!
                         :icon-family :material-icons-outlined
                         :on-click [:storage.media-browser/move-item! file-item]
                         :disabled? true}]
       [elements/button ::duplicate-file-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!
                         :on-click [:storage.media-browser/duplicate-item! file-item]}]
       [elements/button ::rename-file-button
                        {:preset :default-button :icon :edit :indent :left :label :rename!
                         :on-click [:storage.media-browser/rename-item! file-item]}]
       [elements/button ::delete-file-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!
                         :on-click [:storage.media-browser/delete-item! file-item]}]])



;; -- Media-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias content-size items modified-at] :as media-item}]
  [layouts/list-item-a item-dex {:icon           :navigate_next
                                 :label          (str alias)
                                 :description    (media-browser.helpers/directory-item->size   media-item)
                                 :header         (media-browser.helpers/directory-item->header media-item)
                                 :timestamp      (media-browser.helpers/media-item->timestamp  media-item)
                                 :on-click       (on-click-event item-dex media-item)
                                 :on-right-click [:storage.media-browser/render-directory-menu! media-item]}])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias filename] :as media-item}]
  [layouts/list-item-a item-dex {:icon           :more_vert
                                 :label          (str alias)
                                 :description    (media-browser.helpers/file-item->size       media-item)
                                 :header         (media-browser.helpers/file-item->header     media-item)
                                 :timestamp      (media-browser.helpers/media-item->timestamp media-item)
                                 :on-click       (on-click-event item-dex media-item)
                                 :on-right-click [:storage.media-browser/render-file-menu! media-item]}])

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item item-dex media-item]
                                      [file-item      item-dex media-item]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-browser/header :storage.media-browser
                       {:new-item-event   [:storage.media-browser/add-new-item!]
                        :new-item-options [:create-directory! :upload-files!]}])

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [item-browser/body :storage.media-browser
                     {:auto-title?      true
                      :item-actions     [:delete :duplicate]
                      :item-path        [:storage :media-browser/browsed-item]
                      :items-path       [:storage :media-browser/downloaded-items]
                      :label-key :alias :search-keys [:alias]
                      :list-element     #'media-item
                      :root-item-id     core.config/ROOT-DIRECTORY-ID}])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-browser/get-description :storage.media-browser])]
       [layouts/layout-a surface-id {:description description
                                     :header      #'header
                                     :body        #'body}]))
