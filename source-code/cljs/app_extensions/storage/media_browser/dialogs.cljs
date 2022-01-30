
(ns app-extensions.storage.media-browser.dialogs
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-menu
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item]
  [:<> [elements/button ::download-item-button
                        {:preset :default-button :icon :cloud_download :indent :left
                         :label :download!}]
       [elements/button ::download-selected-items-button
                        {:preset :default-button :icon :cloud_download :indent :left
                         :label :download-selected-items!
                         ; És mivel nincs tömörítönk ezért most még disabled lesz
                         :disabled? true}]
       [elements/button ::copy-link-button
                        {:preset :default-button :icon :content_paste :indent :left
                         :label :copy-link!}]
       [elements/button ::make-a-copy-button
                        {:preset :default-button :icon :content_copy :indent :left
                         :label :make-a-copy!}]
       [elements/button ::delete-item-button
                        {:preset :warning-button :icon :delete_outline :indent :left
                         :label :delete!}]])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-file-menu!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ [_ item-dex item]]
  [:ui/add-popup! :storage.media-browser/file-menu
                  {:body [file-menu item-dex item]
                   :min-width :xs
                   :horizontal-align :left}])
