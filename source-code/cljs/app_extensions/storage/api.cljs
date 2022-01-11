
(ns app-extensions.storage.api
    (:require [app-extensions.storage.capacity-handler]
              [app-extensions.storage.dialogs]
              [app-extensions.storage.directory-browser]
              [app-extensions.storage.engine]
              [app-extensions.storage.sync]
              [app-extensions.storage.file-uploader]
              [app-extensions.storage.file-picker :as file-picker]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-extensions.storage.file-picker
(def file-picker file-picker/view)
