
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.events
    (:require [dom.api        :as dom]
              [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-uploader-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id uploader-props]]
  (assoc-in db [:storage :file-uploader/meta-items uploader-id] uploader-props))

(defn init-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [file-selector (dom/get-element-by-id "storage--file-selector")
        files-meta    (dom/file-selector->files-meta file-selector)
        files-data    (dom/file-selector->files-data file-selector)]
       (-> db (assoc-in  [:storage :file-uploader/selected-files uploader-id] files-data)
              (update-in [:storage :file-uploader/meta-items     uploader-id] merge files-meta))))

(defn clean-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (-> db (dissoc-in [:storage :file-uploader/selected-files uploader-id])
         (dissoc-in [:storage :file-uploader/meta-items     uploader-id])))

(defn toggle-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  (let [filesize (get-in db [:storage :file-uploader/selected-files uploader-id file-dex :filesize])]
       (if-let [file-cancelled? (get-in db [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?])]
               (-> db (update-in [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?] not)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :files-size] + filesize)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :file-count]   inc))
               (-> db (update-in [:storage :file-uploader/selected-files uploader-id file-dex :cancelled?] not)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :files-size] - filesize)
                      (update-in [:storage :file-uploader/meta-items     uploader-id :file-count]   dec)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.file-uploader/toggle-file-upload! toggle-file-upload!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.file-uploader/clean-uploader! clean-uploader!)
