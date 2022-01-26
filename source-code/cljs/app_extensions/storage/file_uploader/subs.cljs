
(ns app-extensions.storage.file-uploader.subs
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [mid-fruits.math   :as math]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-sync.api    :as sync]
              [app-extensions.storage.capacity-handler     :as capacity-handler]
              [app-extensions.storage.file-uploader.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

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

(defn all-files-cancelled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (letfn [(f [{:keys [cancelled?]}] (not cancelled?))]
         (not (some f (get-in db [:storage :file-uploader/data-items uploader-id])))))

(defn storage-capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [storage-free-capacity (r capacity-handler/get-storage-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (>= upload-size storage-free-capacity)))

(defn max-upload-size-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [max-upload-size (r capacity-handler/get-max-upload-size db)
        upload-size     (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (> upload-size max-upload-size)))

(defn file-upload-in-progress?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (letfn [(f [[uploader-id _]]
             (let [request-id (engine/request-id uploader-id)]
                  (r sync/request-active? db request-id)))]
         (some f (get-in db [:storage :file-uploader/meta-items]))))

(defn get-notification-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [meta-items (get-in db [:storage :file-uploader/meta-items])]
       {:uploader-ids (-> meta-items map/get-keys vector/reverse-items)}))

(a/reg-sub :storage.file-uploader/get-notification-props get-notification-props)

(defn get-uploader-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [request-id     (engine/request-id uploader-id)
        uploader-props (get-in db [:storage :file-uploader/meta-items uploader-id])]
       (merge uploader-props {:files-uploaded?  (r sync/request-successed? db request-id)
                              :request-sent?    (r sync/request-sent?      db request-id)
                              :request-aborted? (r sync/request-aborted?   db request-id)})))

(a/reg-sub :storage.file-uploader/get-uploader-props get-uploader-props)

(defn get-uploader-progress
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [request-id (engine/request-id uploader-id)]
       (r sync/get-request-progress db request-id)))

(a/reg-sub :storage.file-uploader/get-uploader-progress get-uploader-progress)

(defn get-file-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  (get-in db [:storage :file-uploader/data-items uploader-id file-dex]))

(a/reg-sub :storage.file-uploader/get-file-props get-file-props)

(defn get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  {:files-size                       (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])
   :all-files-cancelled?             (r all-files-cancelled?             db uploader-id)
   :max-upload-size-reached?         (r max-upload-size-reached?         db uploader-id)
   :storage-capacity-limit-exceeded? (r storage-capacity-limit-exceeded? db uploader-id)
   :max-upload-size                  (r capacity-handler/get-max-upload-size       db)
   :storage-free-capacity            (r capacity-handler/get-storage-free-capacity db)})

(a/reg-sub :storage.file-uploader/get-header-props get-header-props)

(defn get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  {:all-files-cancelled? (r all-files-cancelled? db uploader-id)
   :file-count           (r db/get-item-count    db [:storage :file-uploader/data-items uploader-id])})

(a/reg-sub :storage.file-uploader/get-body-props get-body-props)
