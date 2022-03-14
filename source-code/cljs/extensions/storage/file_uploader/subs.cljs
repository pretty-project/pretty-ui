
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.subs
    (:require [app-fruits.dom                           :as dom]
              [extensions.storage.capacity-handler.subs :as capacity-handler.subs]
              [extensions.storage.file-uploader.engine  :as file-uploader.engine]
              [mid-fruits.candy                         :refer [param return]]
              [mid-fruits.map                           :as map]
              [mid-fruits.vector                        :as vector]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-db.api                             :as db]
              [x.app-sync.api                           :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-file-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex prop-key]]
  (get-in db [:storage :file-uploader/data-items uploader-id file-dex prop-key]))

(defn get-file-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (r db/get-item-count db [:storage :file-uploader/data-items uploader-id]))

(defn get-files-size
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (get-in db [:storage :file-uploader/meta-items uploader-id :files-size]))

(defn all-files-cancelled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (letfn [(f [{:keys [cancelled?]}] (not cancelled?))]
         (not (some f (get-in db [:storage :file-uploader/data-items uploader-id])))))

(defn get-non-cancelled-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (letfn [(f [o file-dex {:keys [cancelled?]}]
             (if-not cancelled? (conj   o file-dex)
                                (return o)))]
         (reduce-kv f [] (get-in db [:storage :file-uploader/data-items uploader-id]))))

(defn get-form-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [non-cancelled-files (r get-non-cancelled-files db uploader-id)
        file-selector       (dom/get-element-by-id "storage--file-selector")]
       (dom/file-selector->form-data file-selector non-cancelled-files)))

(defn capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [storage-free-capacity (r capacity-handler.subs/get-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (>= upload-size storage-free-capacity)))

(defn max-upload-size-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [max-upload-size (r capacity-handler.subs/get-max-upload-size db)
        upload-size     (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (> upload-size max-upload-size)))

(defn file-upload-in-progress?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (letfn [(f [[uploader-id _]]
             (let [request-id (file-uploader.engine/request-id uploader-id)]
                  (r sync/request-active? db request-id)))]
         (some f (get-in db [:storage :file-uploader/meta-items]))))

(defn get-uploader-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [meta-items (get-in db [:storage :file-uploader/meta-items])]
       (-> meta-items map/get-keys vector/reverse-items)))

(defn get-uploader-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [request-id (file-uploader.engine/request-id uploader-id)]
       (r sync/get-request-progress db request-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/get-file-prop get-file-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/get-file-count get-file-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/get-files-size get-files-size)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/all-files-cancelled? all-files-cancelled?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/capacity-limit-exceeded? capacity-limit-exceeded?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/max-upload-size-reached? max-upload-size-reached?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/get-uploader-ids get-uploader-ids)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.file-uploader/get-uploader-progress get-uploader-progress)
