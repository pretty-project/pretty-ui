
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
  [{:keys [size items]}]
  (str (-> size io/B->MB format/decimals (str " MB\u00A0\u00A0\u00A0|\u00A0\u00A0\u00A0"))
       (components/content {:content :n-items :replacements [(count items)]})))

(defn file-item->size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [size]}]
  (-> size io/B->MB format/decimals (str " MB")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn directory-item->header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-item]
  (let [icon-family (directory-item->icon-family directory-item)]
       {:icon :folder :icon-family icon-family}))

(defn file-item->header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [alias filename]}]
  {:icon :insert_drive_file
   :thumbnail (if (io/filename->image? alias)
                  (media/filename->media-thumbnail-uri filename))})
