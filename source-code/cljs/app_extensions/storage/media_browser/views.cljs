
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

(defn directory-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn directory-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [items]} _]
  (let [icon-family (if (vector/nonempty? items) :material-icons-filled :material-icons-outlined)]
       [:div.storage--media-item--header [elements/icon {:icon-family icon-family :icon :folder :size :xxl}]]))

(defn directory-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias content-size items]} _]
  [:div.storage--media-item--details
    [elements/label {:content (str alias)
                     :min-height :xs :selectable? true  :color :default}]
    [elements/label {:content (-> content-size io/B->MB format/decimals (str " MB"))
                     :min-height :xs :selectable? false :color :muted
                     :font-size :xs}]
    [elements/label {:content {:content :n-items :replacements [(count items)]} :font-size :xs
                     :min-height :xs :selectable? false :color :muted}]])

(defn directory-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item item-props]
  [:div.storage--media-item [directory-item-header  item-dex item item-props]
                            [directory-item-details item-dex item item-props]
                            [directory-item-icon    item-dex item item-props]])

(defn file-item-preview
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [filename]} _]
  (let [preview-uri (media/filename->media-thumbnail-uri filename)]
       [:div.storage--media-item--preview {:style {:background-image (css/url preview-uri)}}]))

(defn file-item-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ {:keys [icon]}]
  (if icon [:div.storage--media-item--icon [elements/icon {:icon icon}]]))

(defn file-item-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex {:keys [alias] :as item} item-props]
  [:div.storage--media-item--header [elements/icon {:icon :insert_drive_file}]
                                    (if (io/filename->image? alias)
                                        [file-item-preview item-dex item])])

(defn file-item-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ {:keys [alias filesize]} item-props]
  [:div.storage--media-item--details
    [elements/label {:content (str alias)
                     :min-height :xs :selectable? true  :color :default}]
    [elements/label {:content (-> filesize io/B->MB format/decimals (str " MB"))
                     :min-height :xs :selectable? false :color :muted :font-size :xs}]
    [elements/label {:content ""
                     :min-height :xs :selectable? false :color :muted :font-size :xs}]])

(defn file-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex item item-props]
  [:div.storage--media-item [file-item-header  item-dex item item-props]
                            [file-item-details item-dex item item-props]
                            [file-item-icon    item-dex item item-props]])

(defn media-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_ _ item-dex {:keys [mime-type] :as item}]
  (case mime-type "storage/directory" [directory-item item-dex item {:icon :navigate_next}]
                                      [file-item      item-dex item {:icon :more_vert}]))



;; -- View components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [item-browser/view :storage :media {:list-element     #'media-item
                                      :item-actions     [:delete :duplicate]
                                      :new-item-options [:create-directory! :upload-files!]}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  :storage.media-browser/render-browser!
  [:ui/set-surface! :storage.media-browser/view
                    {:view #'view}])
