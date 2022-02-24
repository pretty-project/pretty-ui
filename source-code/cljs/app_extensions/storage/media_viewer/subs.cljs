
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-viewer.subs
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-directory-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  (get-in db [:storage :media-viewer/meta-items :directory-id]))

(defn get-default-item-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  (let [directory-id (r get-directory-id db viewer-id)]
       (get-in db [:storage :media-viewer/data-items directory-id :media/default-item])))

(defn get-current-item-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  (if-let [item-filename (get-in db [:storage :media-viewer/meta-items :current-item])]
          (return item-filename)
          (r get-default-item-filename db viewer-id)))

(defn get-current-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  {:item-filename (r get-current-item-filename db viewer-id)})


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-viewer/get-current-item-props get-current-item-props)
