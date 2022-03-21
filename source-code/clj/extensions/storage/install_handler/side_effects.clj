
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.install-handler.side-effects
    (:require [extensions.storage.core.config            :as core.config]
              [extensions.storage.install-handler.config :as install-handler.config]
              [mongo-db.api                              :as mongo-db]
              [server-fruits.io                          :as io]
              [x.server-core.api                         :as a :refer [r]]
              [x.server-media.api                        :as media]
              [x.server-user.api                         :as user]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [request {:session user/SYSTEM-ACCOUNT}
        options {:prototype-f #(mongo-db/added-document-prototype request :media %)}
        ; Get sample file filesize
        sample-file-filepath (media/filename->media-storage-filepath core.config/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (if-not (mongo-db/get-document-by-id "storage" core.config/SAMPLE-FILE-ID)
               (let [sample-file-document (assoc install-handler.config/SAMPLE-FILE-DOCUMENT :media/filesize sample-file-filesize)]
                    (media/generate-thumbnail! core.config/SAMPLE-FILE-FILENAME)
                    (mongo-db/insert-document! "storage" sample-file-document options)))
       (if-not (mongo-db/get-document-by-id "storage" core.config/ROOT-DIRECTORY-ID)
               (let [root-directory-document (assoc install-handler.config/ROOT-DIRECTORY-DOCUMENT :media/content-size sample-file-filesize)]
                    (mongo-db/insert-document! "storage" root-directory-document options)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :storage/check-install! check-install!)
