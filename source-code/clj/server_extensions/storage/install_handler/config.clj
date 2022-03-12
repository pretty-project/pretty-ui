
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-extensions.storage.install-handler.config
    (:require [server-extensions.storage.config :as config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def ROOT-DIRECTORY-DOCUMENT {:media/id           config/ROOT-DIRECTORY-ID
                              :media/alias        :my-storage
                              :media/description  ""
                              :media/mime-type    "storage/directory"
                              :media/content-size 0
                              :media/items        [{:media/id config/SAMPLE-FILE-ID}]
                              :media/path         []})

; @constant (namespaced map)
(def SAMPLE-FILE-DOCUMENT {:media/id          config/SAMPLE-FILE-ID
                           :media/filename    config/SAMPLE-FILE-FILENAME
                           :media/alias       "Sample.png"
                           :media/description ""
                           :media/mime-type   "image/png"
                           :media/filesize    0
                           :media/path        [{:media/id config/ROOT-DIRECTORY-ID}]})
