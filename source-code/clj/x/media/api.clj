
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.api
    (:require [x.media.core.installer]
              [x.media.download-handler.lifecycles]
              [x.media.thumbnail-handler.lifecycles]
              [x.media.core.helpers                   :as core.helpers]
              [x.media.file-handler.side-effects      :as file-handler.side-effects]
              [x.media.thumbnail-handler.side-effects :as thumbnail-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.media.core.helpers
(def filename->media-storage-uri        core.helpers/filename->media-storage-uri)
(def media-storage-uri->filename        core.helpers/media-storage-uri->filename)
(def filename->media-thumbnail-uri      core.helpers/filename->media-thumbnail-uri)
(def media-thumbnail-uri->filename      core.helpers/media-thumbnail-uri->filename)
(def filename->media-storage-filepath   core.helpers/filename->media-storage-filepath)
(def media-storage-filepath->filename   core.helpers/media-storage-filepath->filename)
(def filename->media-thumbnail-filepath core.helpers/filename->media-thumbnail-filepath)
(def media-thumbnail-filepath->filename core.helpers/media-thumbnail-filepath->filename)
(def filename->temporary-filepath       core.helpers/filename->temporary-filepath)
(def temporary-filepath->filename       core.helpers/temporary-filepath->filename)

; x.media.file-handler.side-effects
(def delete-storage-file!         file-handler.side-effects/delete-storage-file!)
(def delete-storage-thumbnail!    file-handler.side-effects/delete-storage-thumbnail!)
(def duplicate-storage-file!      file-handler.side-effects/duplicate-storage-file!)
(def duplicate-storage-thumbnail! file-handler.side-effects/duplicate-storage-thumbnail!)

; x.media.thumbnail-handler.side-effects
(def generate-thumbnail! thumbnail-handler.side-effects/generate-thumbnail!)
