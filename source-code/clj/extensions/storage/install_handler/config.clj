
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.install-handler.config
    (:require [extensions.storage.core.config :as core.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (namespaced map)
(def ROOT-DIRECTORY-DOCUMENT {:media/id           core.config/ROOT-DIRECTORY-ID
                              :media/alias        :my-storage
                              :media/description  ""
                              :media/mime-type    "storage/directory"
                              :media/content-size 0
                              :media/items        [{:media/id core.config/SAMPLE-FILE-ID}]
                              :media/path         []})

; @constant (namespaced map)
(def SAMPLE-FILE-DOCUMENT {:media/id          core.config/SAMPLE-FILE-ID
                           :media/filename    core.config/SAMPLE-FILE-FILENAME
                           :media/alias       "Sample.png"
                           :media/description ""
                           :media/mime-type   "image/png"
                           :media/filesize    0
                           :media/path        [{:media/id core.config/ROOT-DIRECTORY-ID}]})
