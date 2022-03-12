
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.api
    (:require [x.server-media.download-handler.lifecycles]
              [x.server-media.thumbnail-handler.lifecycles]
              [x.server-media.engine                         :as engine]
              [x.server-media.file-handler.side-effects      :as file-handler.side-effects]
              [x.server-media.thumbnail-handler.side-effects :as thumbnail-handler.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-media.engine
(def allowed-extensions                 engine/allowed-extensions)
(def filename->media-storage-uri        engine/filename->media-storage-uri)
(def filename->media-thumbnail-uri      engine/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   engine/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath engine/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       engine/filename->temporary-filepath)

; x.server-media.file-handler.side-effects
(def delete-storage-file!         file-handler.side-effects/delete-storage-file!)
(def delete-storage-thumbnail!    file-handler.side-effects/delete-storage-thumbnail!)
(def duplicate-storage-file!      file-handler.side-effects/duplicate-storage-file!)
(def duplicate-storage-thumbnail! file-handler.side-effects/duplicate-storage-thumbnail!)

; x.server-media.thumbnail-handler.side-effects
(def generate-thumbnail! thumbnail-handler.side-effects/generate-thumbnail!)
