
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-browser.views
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.css       :as css]
              [mid-fruits.format    :as format]
              [mid-fruits.io        :as io]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-elements.api   :as elements]
              [x.app-media.api      :as media]
              [app-plugins.item-browser.api :as item-browser]))



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
  [media-item item-props]
  [:div.storage--media-item [directory-item-header  media-item item-props]
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
  [media-item item-props]
  [:div.storage--media-item [file-item-header  media-item item-props]
                            [file-item-details media-item item-props]
                            [file-item-icon    media-item item-props]])

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ _ {:keys [mime-type] :as media-item}]
  (case mime-type "storage/directory" [directory-item media-item {:icon :navigate_next}]
                                      [file-item      media-item {:icon :more_vert}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-browser/view :storage :media {:list-element     #'media-item
                                      :item-actions     [:delete :duplicate]
                                      :new-item-options [:create-directory! :upload-files!]}])
