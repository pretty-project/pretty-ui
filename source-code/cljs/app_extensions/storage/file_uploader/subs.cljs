
(ns app-extensions.storage.file-uploader.subs
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.math    :as math]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-sync.api     :as sync]
              [app-extensions.storage.capacity-handler :as capacity-handler]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-non-aborted-files
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (letfn [(f [o file-dex {:keys [aborted?]}]
             (if-not aborted? (conj   o file-dex)
                              (return o)))]
         (reduce-kv f [] (get-in db [:storage :file-uploader/data-items uploader-id]))))

(defn- get-form-data
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [non-aborted-files (r get-non-aborted-files db uploader-id)
        file-selector     (dom/get-element-by-id "storage--file-selector")]
       (dom/file-selector->form-data file-selector non-aborted-files)))

(defn- all-files-aborted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (letfn [(f [{:keys [aborted?]}] (not aborted?))]
         (not (some f (get-in db [:storage :file-uploader/data-items uploader-id])))))

(defn- storage-capacity-limit-exceeded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [storage-free-capacity (r capacity-handler/get-storage-free-capacity db)
        upload-size           (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (>= upload-size storage-free-capacity)))

(defn- max-upload-size-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [max-upload-size (r capacity-handler/get-max-upload-size db)
        upload-size     (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])]
       (> upload-size max-upload-size)))

(defn- get-progress-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (println (str "Let's see how many times it happen ... "
                (r sync/get-request-progress db (keyword/add-namespace :storage uploader-id))))
  (letfn [(f [result uploader-id {:keys [file-count files-size]}]
             (let [request-id       (keyword/add-namespace :storage uploader-id)
                   request-progress (r sync/get-request-progress db request-id)]
                  (-> result (update :file-count    + file-count)
                             (update :total-size    + files-size)
                             (update :uploaded-size + (math/percent-result files-size request-progress)))))]
         (reduce-kv f {} (get-in db [:storage :file-uploader/meta-items]))))


;  (let [request-id       (keyword/add-namespace :storage uploader-id)
;        request-progress (r sync/get-request-progress db request-id)
;       {:file-count       (get-in db [:storage :file-uploader/meta-items uploader-id :file-count])
;        :files-size       (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])
;        :files-uploaded?  (= request-progress 100)
;        :request-progress (param request-progress)
;        :upload-failured? (r sync/request-failured? db request-id)))

(a/reg-sub :storage/get-progress-props get-progress-props)

(defn- get-file-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  (get-in db [:storage :file-uploader/data-items uploader-id file-dex]))

(a/reg-sub :storage/get-file-uploader-file-props get-file-props)

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  {:files-size                       (get-in db [:storage :file-uploader/meta-items uploader-id :files-size])
   :all-files-aborted?               (r all-files-aborted?               db uploader-id)
   :max-upload-size-reached?         (r max-upload-size-reached?         db uploader-id)
   :storage-capacity-limit-exceeded? (r storage-capacity-limit-exceeded? db uploader-id)
   :max-upload-size                  (r capacity-handler/get-max-upload-size       db)
   :storage-free-capacity            (r capacity-handler/get-storage-free-capacity db)})

(a/reg-sub :storage/get-file-uploader-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  {:all-files-aborted? (r all-files-aborted? db uploader-id)
   :file-count         (r db/get-item-count db [:storage :file-uploader/data-items uploader-id])})

(a/reg-sub :storage/get-file-uploader-body-props get-body-props)
