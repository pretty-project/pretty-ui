
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.08
; Description:
; Version: v0.5.2
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.api
    (:require [x.server-media.download-handler]
              [x.server-media.engine            :as engine]
              [x.server-media.temporary-handler :as temporary-handler]
              [x.server-media.thumbnail-handler :as thumbnail-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-media.engine
(def allowed-extensions                 engine/allowed-extensions)
(def filename->media-storage-uri        engine/filename->media-storage-uri)
(def filename->media-thumbnail-uri      engine/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   engine/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath engine/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       engine/filename->temporary-filepath)
(def delete-storage-file!               engine/delete-storage-file!)
(def delete-storage-thumbnail!          engine/delete-storage-thumbnail!)
(def duplicate-storage-file!            engine/duplicate-storage-file!)
(def duplicate-storage-thumbnail!       engine/duplicate-storage-thumbnail!)

; x.server-media.temporary-handler
(def download-temporary-file temporary-handler/download-temporary-file)

; x.server-media.thumbnail-handler
(def generate-thumbnail! thumbnail-handler/generate-thumbnail!)
