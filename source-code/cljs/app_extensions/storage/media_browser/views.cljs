
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-browser.views
    (:require [app-plugins.item-browser.api :as item-browser]
              [mid-fruits.css               :as css]
              [mid-fruits.format            :as format]
              [mid-fruits.io                :as io]
              [mid-fruits.vector            :as vector]
              [x.app-components.api         :as components]
              [x.app-core.api               :as a :refer [r]]
              [x.app-elements.api           :as elements]
              [x.app-layouts.api            :as layouts]
              [x.app-media.api              :as media]
              [x.app-ui.api                 :as ui]))



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

(defn media-item-alias-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias]} _]
  [elements/label {:min-height :xs :content alias}])

(defn media-item-modified-at-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [modified-at]} _]
  [elements/label {:content @(a/subscribe [:activities/get-actual-timestamp modified-at])
                   :font-size :xs :min-height :xs :selectable? false :color :muted}])

(defn directory-item-content-size-labels
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [content-size items]} _]
  [:div {:style {:display :flex}}
        [elements/label {:content (-> content-size io/B->MB format/decimals (str " MB"))
                         :font-size :xs :min-height :xs :selectable? false :color :muted}]
        [elements/label {:font-size :xs :min-height :xs :selectable? false :color :muted :indent :both :content "|"}]
        [elements/label {:content {:content :n-items :replacements [(count items)] :prefix "" :suffix ""}
                         :font-size :xs :min-height :xs :selectable? false :color :muted}]])

(defn file-item-filesize-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [filesize]} _]
  [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                   :min-height :xs :selectable? false :color :muted :font-size :xs}])

(defn directory-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn directory-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [items]} _]
  (let [icon-family (if (vector/nonempty? items) :material-icons-filled :material-icons-outlined)]
       [:div.storage--media-item--header [elements/icon {:icon-family icon-family :icon :folder :size :xxl}]]))

(defn directory-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias content-size items] :as media-item} item-props]
  [:div.storage--media-item--details
    [media-item-alias-label             media-item item-props]
    [media-item-modified-at-label       media-item item-props]
    [directory-item-content-size-labels media-item item-props]])

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item {:keys [disabled?] :as item-props}]
  [:div.storage--media-item {:data-disabled (boolean disabled?)}
                            [directory-item-header  media-item item-props]
                            [directory-item-details media-item item-props]
                            [directory-item-icon    media-item item-props]])

(defn file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [filename]} _]
  (let [preview-uri (media/filename->media-thumbnail-uri filename)]
       [:div.storage--media-item--preview {:style {:background-image (css/url preview-uri)}}]))

(defn file-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias] :as media-item} item-props]
  [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]
                                    (if (io/filename->image? alias)
                                        [file-item-preview media-item item-props])])

(defn file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias filesize] :as media-item} item-props]
  [:div.storage--media-item--details
    [media-item-alias-label       media-item item-props]
    [media-item-modified-at-label media-item item-props]
    [file-item-filesize-label     media-item item-props]])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [media-item {:keys [disabled?] :as item-props}]
  [:div.storage--media-item {:data-disabled (boolean disabled?)}
                            [file-item-header  media-item item-props]
                            [file-item-details media-item item-props]
                            [file-item-icon    media-item item-props]])

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ item-dex {:keys [id mime-type] :as media-item}]
  (case mime-type "storage/directory" [elements/toggle {:content        [directory-item media-item {:icon :navigate_next}]
                                                        :on-click       [:storage.media-browser/item-clicked item-dex media-item]
                                                        :on-right-click [:storage.media-browser/render-directory-menu! media-item]
                                                        :hover-color :highlight}]
                                      [elements/toggle {:content        [file-item media-item {:icon :more_vert}]
                                                        :on-click       [:storage.media-browser/item-clicked item-dex media-item]
                                                        :on-right-click [:storage.media-browser/render-file-menu! media-item]
                                                        :hover-color :highlight}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  (let [description @(a/subscribe [:item-browser/get-description :storage :media])]
       [layouts/layout-a surface-id {:header [item-browser/header :storage :media {:new-item-options [:create-directory! :upload-files!]}]
                                     :body   [item-browser/body   :storage :media {:item-actions     [:delete :duplicate]
                                                                                   :list-element     #'media-item}]
                                     :description description}]))
