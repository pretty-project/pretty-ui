
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.views
    (:require [extensions.storage.core.config            :as core.config]
              [extensions.storage.media-browser.helpers  :as media-browser.helpers]
              [extensions.storage.media-selector.helpers :as media-selector.helpers]
              [plugins.item-browser.api                  :as item-browser]
              [x.app-core.api                            :as a]
              [x.app-elements.api                        :as elements]
              [x.app-ui.api                              :as ui]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  (let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-selector])
        on-save       [:storage.media-selector/save-selected-items!]]
       [:<> [ui/save-popup-header :storage.media-selector/view {:label header-label :on-save on-save}]
            [item-browser/header  :storage.media-selector
                                  {:new-item-event   [:storage.media-selector/add-new-item!]
                                   :new-item-options [:create-directory! :upload-files!]}]]))



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  (let [selected-item-count @(a/subscribe [:storage.media-selector/get-selected-item-count])]
       [ui/selection-popup-footer :storage.media-selector/view
                                  {:on-discard [:storage.media-selector/discard-selection!]
                                   :selected-item-count selected-item-count}]))



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias id modified-at] :as media-item}]
  [item-browser/list-item :storage.media-selector item-dex
                          {:icon        :navigate_next
                           :label       (str alias)
                           :description (media-browser.helpers/directory-item->size   media-item)
                           :header      (media-browser.helpers/directory-item->header media-item)
                           :on-click    [:item-browser/browse-item! :storage.media-selector id]
                           :timestamp   modified-at}])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias modified-at] :as media-item}]
  (let [file-selectable? @(a/subscribe [:storage.media-selector/file-selectable? media-item])]
       [item-browser/list-item :storage.media-selector item-dex
                               {:label       (str alias)
                                :disabled?   (not file-selectable?)
                                :description (media-browser.helpers/file-item->size            media-item)
                                :header      (media-browser.helpers/file-item->header          media-item)
                                :icon        (media-selector.helpers/file-item->selection-icon media-item)
                                :on-click    [:storage.media-selector/file-clicked             media-item]
                                :timestamp   modified-at}]))

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item item-dex media-item]
                                      [file-item      item-dex media-item]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [item-browser/body :storage.media-selector
                     {:item-path    [:storage :media-selector/browsed-item]
                      :items-path   [:storage :media-selector/downloaded-items]
                      :items-key    :items
                      :label-key    :alias
                      :path-key     :path
                      :list-element #'media-item
                      :root-item-id core.config/ROOT-DIRECTORY-ID
                      :search-keys  [:alias]}])
