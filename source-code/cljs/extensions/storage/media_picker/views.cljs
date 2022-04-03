
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.views
    (:require [extensions.storage.core.config           :as core.config]
              [extensions.storage.media-browser.helpers :as media-browser.helpers]
              [extensions.storage.media-picker.helpers  :as media-picker.helpers]
              [plugins.item-browser.api                 :as item-browser]
              [plugins.item-browser.core.views          :as plugins.item-browser.core.views]
              [plugins.item-lister.core.views           :as plugins.item-lister.core.views]
              [x.app-core.api                           :as a]
              [x.app-elements.api                       :as elements]
              [x.app-layouts.api                        :as layouts]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/button :header-cancel-button
                   {:preset :cancel-button :indent :both :keypress {:key-code 27}
                    :on-click [:ui/close-popup! :storage.media-picker/view]}])

(defn header-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [header-label @(a/subscribe [:item-browser/get-current-item-label :storage.media-picker])]
       [elements/label ::header-label
                       {:content header-label}]))

(defn header-select-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected? @(a/subscribe [:storage.media-picker/no-items-selected?])]
       [elements/button :header-select-button
                        {:disabled? no-items-selected?
                         :preset :select-button :indent :both :keypress {:key-code 13}
                         :on-click [:storage.media-picker/save-selected-items!]}]))

(defn header-label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button picker-id]
                                 :middle-content [header-label         picker-id]
                                 :end-content    [header-select-button picker-id]}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [:<> [header-label-bar picker-id]
       [item-browser/header :storage.media-picker
                            {:new-item-event   [:storage.media-picker/add-new-item!]
                             :new-item-options [:create-directory! :upload-files!]}]])



;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn footer-selection-bar-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [no-items-selected?  @(a/subscribe [:storage.media-picker/no-items-selected?])
        selected-item-count @(a/subscribe [:storage.media-picker/get-selected-item-count])]
       [:<> [elements/label {:content {:content :n-items-selected :replacements [selected-item-count]}
                             :color :muted :min-height :l :font-size :xs}]
            [elements/icon-button {:color :default :height :l :preset :close :disabled? no-items-selected?
                                   :on-click [:storage.media-picker/discard-selection!]}]]))

(defn footer-selection-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [elements/row {:content [footer-selection-bar-structure picker-id]
                 :horizontal-align :right}])

(defn footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [footer-selection-bar picker-id])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias id] :as media-item}]
  [layouts/list-item-b item-dex {:label alias
                                 :icon :navigate_next
                                 :size      (media-browser.helpers/directory-item->size      media-item)
                                 :thumbnail (media-browser.helpers/directory-item->thumbnail media-item)
                                 :timestamp (media-browser.helpers/media-item->timestamp     media-item)
                                 :on-click  [:item-browser/browse-item! :storage.media-picker id]}])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias] :as media-item}]
  (let [file-selectable? @(a/subscribe [:storage.media-picker/file-selectable? media-item])]
       [layouts/list-item-b item-dex {:label     alias
                                      :disabled? (not file-selectable?)
                                      :icon      (media-picker.helpers/file-item->selection-icon media-item)
                                      :size      (media-browser.helpers/file-item->size          media-item)
                                      :thumbnail (media-browser.helpers/file-item->thumbnail     media-item)
                                      :timestamp (media-browser.helpers/media-item->timestamp    media-item)
                                      :on-click  [:storage.media-picker/file-clicked media-item]}]))

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [browser-id item-dex {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item item-dex media-item]
                                      [file-item      item-dex media-item]))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id]
  [item-browser/body :storage.media-picker
                     {:item-path     [:storage :media-picker/browsed-item]
                      :items-path    [:storage :media-picker/downloaded-items]
                      :label-key     :alias
                      :list-element  #'media-item
                      :root-item-id  core.config/ROOT-DIRECTORY-ID
                      :search-keys   [:alias]}])



;; -- Element components ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- n-items-selected-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [multiple?]}]
  (let [no-items-picked?  @(a/subscribe [:storage.media-picker/no-items-picked?])
        picked-item-count @(a/subscribe [:storage.media-picker/get-picked-item-count])]
       [elements/label {:color :muted :min-height :s
                        :content (cond (and multiple? no-items-picked?) :no-items-selected
                                       no-items-picked?                 :no-item-selected
                                       :default {:content :n-items-selected :replacements [picked-item-count]})}]))

(defn media-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [disabled? indent label]}]
  (if label [elements/label {:content label :min-height :s :disabled? disabled? :indent indent}]))

(defn media-picker-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id {:keys [disabled? indent] :as picker-props}]
  [elements/toggle {:content  [n-items-selected-label picker-id]
                    :on-click [:storage.media-picker/load-picker! picker-id picker-props]
                    :disabled? disabled?
                    :indent    indent}])

(defn media-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [picker-id picker-props]
  [:<> [media-picker-label  picker-id picker-props]
       [media-picker-button picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :multiple? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [storage/media-picker {...}]
  ;
  ; @usage
  ;  [storage/media-picker :my-picker {...}]
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   [media-picker picker-id picker-props]))
