
(ns app-extensions.storage.media-browser.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.io      :as io]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [x.app-ui.api       :as ui]))



;; -- File-menu components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias] :as file-item}]
  [elements/label {:color :muted :indent :left :content alias :font-size :xs}])

(defn file-menu-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex file-item]
  [elements/horizontal-polarity {:start-content [file-menu-label item-dex file-item]
                                 :end-content   [ui/popup-close-icon-button :storage.media-browser/file-menu {}]}])

(defn file-menu-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [mime-type] :as file-item}]
  [:<> (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:preset :default-button :icon :preview :indent :left :label :file-preview
                             :on-click [:storage.media-browser/preview-file! item-dex file-item]}])
       [elements/button ::download-file-button
                        {:preset :default-button :icon :cloud_download :indent :left :label :download!
                         :on-click [:storage.media-browser/download-file! item-dex file-item]}]
       [elements/button ::copy-file-link-button
                        {:preset :default-button :icon :content_paste :indent :left :label :copy-link!
                         :on-click [:storage.media-browser/copy-file-link! item-dex file-item]}]
       [elements/button ::duplicate-file-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!}]
       [elements/button ::rename-file-button
                        {:preset :default-button :icon :edit :indent :left :label :rename!}]
       [elements/button ::more-info-button
                        {:preset :default-button :icon :delete_outline :indent :left :label :more-info}]
       [elements/button ::delete-file-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-directory-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [db]} [_ item-dex directory-item]]
  [:ui/add-popup! :storage.media-browser/directory-menu
                  {:body   [file-menu-body   item-dex directory-item]
                   :header [file-menu-header item-dex directory-item]
                   :horizontal-align :left
                   :min-width        :xs}])

(defn render-file-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [db]} [_ item-dex file-item]]
  [:ui/add-popup! :storage.media-browser/file-menu
                  {:body   [file-menu-body   item-dex file-item]
                   :header [file-menu-header item-dex file-item]
                   :horizontal-align :left
                   :min-width        :xs}])
