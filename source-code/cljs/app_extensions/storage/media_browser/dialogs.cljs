
(ns app-extensions.storage.media-browser.dialogs
    (:require [mid-fruits.io      :as io]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]))



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
       [elements/button ::duplicate-directory-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!
                         :on-click [:storage.media-browser/duplicate-file! directory-item]}]
       [elements/button ::rename-directory-button
                        {:preset :default-button :icon :edit :indent :left :label :rename!
                         :on-click [:storage.media-browser/rename-directory! directory-item]}]
       [elements/button ::delete-directory-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!
                         :on-click [:storage.media-browser/delete-directory! directory-item]}]])



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
       [elements/button ::duplicate-file-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!
                         :on-click [:storage.media-browser/duplicate-file! file-item]}]
       [elements/button ::rename-file-button
                        {:preset :default-button :icon :edit :indent :left :label :rename!
                         :on-click [:storage.media-browser/rename-file! file-item]}]
       [elements/button ::delete-file-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-rename-directory-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ [_ {:keys [alias] :as directory-item}]]
  [:value-editor/load-editor! :storage :file-name
                              {:label :directory-name :save-button-label :rename! :initial-value alias
                               :on-save   [:storage.media-browser/update-item-alias! directory-item]
                               :validator {:f io/directory-name-valid?
                                           :invalid-message :invalid-directory-name
                                           :pre-validate?   true}}])

(defn render-rename-file-dialog!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ [_ {:keys [alias] :as file-item}]]
  [:value-editor/load-editor! :storage :file-name
                              {:label :file-name :save-button-label :rename! :initial-value alias
                               :on-save   [:storage.media-browser/update-item-alias! file-item]
                               :validator {:f io/filename-valid?
                                           :invalid-message :invalid-file-name
                                           :pre-validate?   true}}])

(defn render-directory-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [db]} [_ directory-item]]
  [:ui/add-popup! :storage.media-browser/media-menu
                  {:body   [directory-menu-body directory-item]
                   :header [media-menu-header   directory-item]
                   :horizontal-align :left
                   :min-width        :xs}])

(defn render-file-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [db]} [_ file-item]]
  [:ui/add-popup! :storage.media-browser/media-menu
                  {:body   [file-menu-body    file-item]
                   :header [media-menu-header file-item]
                   :horizontal-align :left
                   :min-width        :xs}])
