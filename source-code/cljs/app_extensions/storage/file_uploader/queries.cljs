
(ns app-extensions.storage.file-uploader.queries
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-upload-files-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ uploader-id]]
  (let [destination-id (get-in db [:storage :file-uploader/meta-items uploader-id :destination-id])]
       [:debug `(storage/upload-files! ~{:destination-id destination-id})]))
