
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.views
    (:require [extensions.storage.core.config            :as core.config]
              [extensions.storage.media-browser.helpers  :as media-browser.helpers]
              [extensions.storage.media-selector.helpers :as media-selector.helpers]
              [plugins.item-browser.api                  :as item-browser]
              [x.app-core.api                            :as a]
              [x.app-elements.api                        :as elements]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! :storage.media-selector/view]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-selector])]
       [elements/label ::header-label
                       {:content header-label}]))

(defn header-save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button :header-save-button
                   {:preset :save-button :indent :both :keypress {:key-code 13}
                    :on-click [:storage.media-selector/save-selected-items!]}])

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button selector-id]
                                 :middle-content [header-label         selector-id]
                                 :end-content    [header-save-button   selector-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [:<> [header-label-bar selector-id]
       [item-browser/header :storage.media-selector
                            {:new-item-event   [:storage.media-selector/add-new-item!]
                             :new-item-options [:create-directory! :upload-files!]}]])



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-selection-bar-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected?  @(a/subscribe [:storage.media-selector/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-selector/get-selected-item-count])]
       [:<> [elements/label {:content {:content :n-items-selected :replacements [selected-item-count]}
                             :color :muted :min-height :l :font-size :xs}]
            [elements/icon-button {:color :default :height :l :preset :close :disabled? no-items-selected?
                                   :on-click [:storage.media-selector/discard-selection!]}]]))

(defn footer-selection-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [elements/row {:content [footer-selection-bar-structure selector-id]
                 :horizontal-align :right}])

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [footer-selection-bar selector-id])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias id] :as media-item}]
  [item-browser/list-item item-dex {:icon :navigate_next
                                    :label       (str alias)
                                    :description (media-browser.helpers/directory-item->size   media-item)
                                    :header      (media-browser.helpers/directory-item->header media-item)
                                    :timestamp   (media-browser.helpers/media-item->timestamp  media-item)
                                    :on-click    [:item-browser/browse-item! :storage.media-selector id]}])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias] :as media-item}]
  (let [file-selectable? @(a/subscribe [:storage.media-selector/file-selectable? media-item])]
       [item-browser/list-item item-dex {:label       (str alias)
                                         :disabled?   (not file-selectable?)
                                         :description (media-browser.helpers/file-item->size            media-item)
                                         :header      (media-browser.helpers/file-item->header          media-item)
                                         :icon        (media-selector.helpers/file-item->selection-icon media-item)
                                         :timestamp   (media-browser.helpers/media-item->timestamp      media-item)
                                         :on-click    [:storage.media-selector/file-clicked             media-item]}]))

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item item-dex media-item]
                                      [file-item      item-dex media-item]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [selector-id]
  [item-browser/body :storage.media-selector
                     {:item-path     [:storage :media-selector/browsed-item]
                      :items-path    [:storage :media-selector/downloaded-items]
                      :label-key     :alias
                      :list-element  #'media-item
                      :root-item-id  core.config/ROOT-DIRECTORY-ID
                      :search-keys   [:alias]}])
