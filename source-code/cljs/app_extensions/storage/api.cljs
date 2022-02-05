
(ns app-extensions.storage.api
    (:require [app-extensions.storage.capacity-handler]
              [app-extensions.storage.directory-creator.api]
              [app-extensions.storage.engine]
              [app-extensions.storage.file-uploader.api]
              [app-extensions.storage.media-browser.api]
              [app-extensions.storage.media-picker.api :as media-picker]
              [app-extensions.storage.media-viewer.api :as media-viewer]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-extensions.storage.media-picker.api
(def media-picker media-picker/element)

; app-extensions.storage.media-viewer.api
(def media-viewer media-viewer/element)
