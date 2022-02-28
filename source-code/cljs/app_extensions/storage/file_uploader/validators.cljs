
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.validators
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-files-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id server-response]]
  (let [file-count (get-in db [:storage :file-uploader/meta-items uploader-id :file-count])
        response   (get    server-response `storage.file-uploader/upload-files!)]
       (vector/count? response file-count)))
