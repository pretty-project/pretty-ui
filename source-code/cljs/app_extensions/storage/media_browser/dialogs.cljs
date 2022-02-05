
(ns app-extensions.storage.media-browser.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.io      :as io]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]
              [x.app-media.api    :as media]
              [app-extensions.storage.media-browser.subs :as subs]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias mime-type] :as file-item}]
  [:<> [elements/label ::file-alias-label
                       {:content alias :horizontal-align :center :color :muted :indent :left}]
       ;[elements/horizontal-line {:color :muted}]
       (if (or (io/mime-type->image? mime-type)
               (= mime-type "application/pdf"))
           [elements/button ::preview-file-button
                            {:preset :default-button :icon :preview :indent :left :label :file-preview
                             :on-click [:storage.media-browser/preview-file! file-item]}])
       [elements/button ::download-file-button
                        {:preset :default-button :icon :cloud_download :indent :left :label :download!}]
       [elements/button ::copy-file-link-button
                        {:preset :default-button :icon :content_paste :indent :left :label :copy-link!
                         :on-click [:storage.media-browser/copy-file-link! file-item]}]
       [elements/button ::duplicate-file-button
                        {:preset :default-button :icon :content_copy :indent :left :label :duplicate!}]
       [elements/button ::more-info-button
                        {:preset :default-button :icon :delete_outline :indent :left :label :more-info}]
       [elements/button ::delete-file-button
                        {:preset :warning-button :icon :delete_outline :indent :left :label :delete!}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-file-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [db]} [_ file-item]]
  [:ui/add-popup! :storage.media-browser/file-menu
                  {:body [file-menu file-item]
                   :horizontal-align :left
                   :min-width        :xs}])
