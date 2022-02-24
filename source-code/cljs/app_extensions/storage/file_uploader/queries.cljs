
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.file-uploader.queries
    (:require [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-upload-files-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
       [:debug `(storage.file-uploader/upload-files! ~{:destination-id destination-id})]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upload-files-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id server-response]]
  (let [file-count (get-in db [:storage :file-uploader/meta-items uploader-id :file-count])
        response   (get    server-response `storage.file-uploader/upload-files!)]
       (vector/count? response file-count)))
