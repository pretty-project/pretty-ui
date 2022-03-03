
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.installer.side-effects
    (:require [mid-fruits.candy                           :refer [param return]]
              [mongo-db.api                               :as mongo-db]
              [server-extensions.storage.engine           :as engine]
              [server-extensions.storage.installer.engine :as installer.engine]
              [server-fruits.io                           :as io]
              [x.server-core.api                          :as a :refer [r]]
              [x.server-media.api                         :as media]
              [x.server-user.api                          :as user]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [request {:session user/SYSTEM-ACCOUNT}
        options {:prototype-f #(mongo-db/added-document-prototype request :media %)}
        ; Get sample file filesize
        sample-file-filepath (media/filename->media-storage-filepath engine/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (if-not (mongo-db/get-document-by-id "storage" engine/SAMPLE-FILE-ID)
               (let [sample-file-document (assoc installer.engine/SAMPLE-FILE-DOCUMENT :media/filesize sample-file-filesize)]
                    (media/generate-thumbnail! engine/SAMPLE-FILE-FILENAME)
                    (mongo-db/insert-document! "storage" sample-file-document options)))
       (if-not (mongo-db/get-document-by-id "storage" engine/ROOT-DIRECTORY-ID)
               (let [root-directory-document (assoc installer.engine/ROOT-DIRECTORY-DOCUMENT :media/content-size sample-file-filesize)]
                    (mongo-db/insert-document! "storage" root-directory-document options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :storage/check-install! check-install!)
