
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.installer.engine
    (:require [server-extensions.storage.engine :as engine]))



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
