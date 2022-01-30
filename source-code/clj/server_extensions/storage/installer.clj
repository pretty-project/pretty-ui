
(ns server-extensions.storage.installer
    (:require [mid-fruits.candy   :refer [param return]]
              [mongo-db.api       :as mongo-db]
              [server-fruits.io   :as io]
              [x.server-core.api  :as a :refer [r]]
              [x.server-media.api :as media]
              [x.server-user.api  :as user]
              [server-extensions.storage.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def ROOT-DIRECTORY-DOCUMENT {:media/id           engine/ROOT-DIRECTORY-ID
                              :media/alias        :my-storage
                              :media/description  ""
                              :media/mime-type    "storage/directory"
                              :media/content-size 0
                              :media/items        [{:media/id engine/SAMPLE-FILE-ID}]
                              :media/path         []})

; @constant (namespaced map)
(def SAMPLE-FILE-DOCUMENT {:media/id          engine/SAMPLE-FILE-ID
                           :media/filename    engine/SAMPLE-FILE-FILENAME
                           :media/alias       "Sample.png"
                           :media/description ""
                           :media/mime-type   "image/png"
                           :media/filesize    0
                           :media/path        [{:media/id engine/ROOT-DIRECTORY-ID}]})



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
               (let [sample-file-document (assoc SAMPLE-FILE-DOCUMENT :media/filesize sample-file-filesize)]
                    (media/generate-thumbnail! engine/SAMPLE-FILE-FILENAME)
                    (mongo-db/insert-document! "storage" sample-file-document options)))
       (if-not (mongo-db/get-document-by-id "storage" engine/ROOT-DIRECTORY-ID)
               (let [root-directory-document (assoc ROOT-DIRECTORY-DOCUMENT :media/content-size sample-file-filesize)]
                    (mongo-db/insert-document! "storage" root-directory-document options)))))

(a/reg-fx :storage/check-install! check-install!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-launch {:storage/check-install! nil}})
