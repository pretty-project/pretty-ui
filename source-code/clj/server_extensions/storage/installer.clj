
(ns server-extensions.storage.installer
    (:require [mid-fruits.candy   :refer [param return]]
              [mongo-db.api       :as mongo-db]
              [server-fruits.io   :as io]
              [x.app-details      :as details]
              [x.server-core.api  :as a :refer [r]]
              [x.server-media.api :as media]
              [x.server-user.api  :as user]
              [server-extensions.storage.directory-browser :as directory-browser]
              [server-extensions.storage.engine            :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def ROOT-DIRECTORY-DOCUMENT {:directory/id           engine/ROOT-DIRECTORY-ID
                              :directory/alias        ":my-storage"
                              :directory/description  ""
                              :directory/content-size 0
                              :directory/colors       []
                              :directory/items        []
                              :directory/path         []})

; @constant (namespaced map)
(def SAMPLE-FILE-DOCUMENT {:file/id          engine/SAMPLE-FILE-ID
                           :file/filename    engine/SAMPLE-FILE-FILENAME
                           :file/description ""
                           :file/filesize    0
                           :file/colors     []
                           :file/path       [{:directory/id engine/ROOT-DIRECTORY-ID}]})



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-root-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [sample-file-filepath (media/filename->media-storage-filepath engine/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (directory-browser/add-directory-item-f {:request {:session user/SYSTEM-ACCOUNT}}
                                               (assoc ROOT-DIRECTORY-DOCUMENT :directory/content-size sample-file-filesize))))

(defn- add-sample-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [sample-file-filepath (media/filename->media-storage-filepath engine/SAMPLE-FILE-FILENAME)
        sample-file-filesize (io/get-filesize sample-file-filepath)]
       (directory-browser/add-file-item-f {:request {:session user/SYSTEM-ACCOUNT}}
                                          (assoc SAMPLE-FILE-DOCUMENT :file/filesize sample-file-filesize))))

(defn- check-install!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (if-let [root-directory-document (mongo-db/get-document-by-id "directories" engine/ROOT-DIRECTORY-ID)]
          (let [root-directory-added-at (get root-directory-document :directory/added-at)])
             ;(println "[:storage] installed at:" root-directory-added-at)
          (do (println "[:storage] Installing extension ...")
              (add-root-directory!)
              (add-sample-file!))))

(a/reg-fx :storage/check-install! check-install!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-server-launch {:storage/check-install! nil}})
