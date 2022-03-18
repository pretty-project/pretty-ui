
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
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id] files-data)
              (update-in [:storage :file-uploader/meta-items uploader-id] merge files-meta))))

(defn clean-uploader!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (-> db (dissoc-in [:storage :file-uploader/data-items uploader-id])
         (dissoc-in [:storage :file-uploader/meta-items uploader-id])))

(defn cancel-file-upload!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id file-dex]]
  ; A feltöltendő fájlok adatai közül nem kerülnek törlésre a visszavont fájlok adatai,
  ; mert a változó elemszám miatt a feltöltendő fájlok listája újra renderelődne!
  (let [filesize (get-in db [:storage :file-uploader/data-items uploader-id file-dex :filesize])]
       (-> db (assoc-in  [:storage :file-uploader/data-items uploader-id file-dex :cancelled?] true)
              (update-in [:storage :file-uploader/meta-items uploader-id :files-size] - filesize)
              (update-in [:storage :file-uploader/meta-items uploader-id :file-count]   dec))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.file-uploader/cancel-file-upload! cancel-file-upload!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.file-uploader/clean-uploader! clean-uploader!)
