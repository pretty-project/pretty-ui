
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-browser.helpers
    (:require [mid-fruits.format    :as format]
              [mid-fruits.io        :as io]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-media.api      :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item->icon-family
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [items]}]
  (if (vector/nonempty? items) :material-icons-filled :material-icons-outlined))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item->size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [content-size items]}]
  (str (-> content-size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
       (components/content {:content :n-items :replacements [(count items)]})))

(defn file-item->size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [filesize]}]
  (-> filesize io/B->MB format/decimals (str " MB")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-item->timestamp
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [modified-at]}]
  @(a/subscribe [:activities/get-actual-timestamp modified-at]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item->thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-item]
  (let [icon-family (directory-item->icon-family directory-item)]
       {:icon :folder :icon-family icon-family}))

(defn file-item->thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias filename]}]
  {:icon :insert_drive_file
   :uri (if (io/filename->image? alias)
            (media/filename->media-thumbnail-uri filename))})
