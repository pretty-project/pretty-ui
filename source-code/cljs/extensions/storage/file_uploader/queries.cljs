
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.queries)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-upload-files-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
       [`(storage.file-uploader/upload-files! ~{:destination-id destination-id})]))
