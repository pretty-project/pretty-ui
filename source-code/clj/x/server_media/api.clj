
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.08.08
; Description:
; Version: v0.4.0
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.api
    (:require [x.server-media.download-handler  :as download-handler]
              [x.server-media.engine            :as engine]
              [x.server-media.item-handler      :as item-handler]
              [x.server-media.temporary-handler :as temporary-handler]
              [x.server-media.thumbnail-handler :as thumbnail-handler]
              [x.server-media.upload-handler    :as upload-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-media/download-handler
(def download-file download-handler/download-file)

; x.server-media.engine
(def allowed-extensions                 engine/allowed-extensions)
(def filename->media-storage-uri        engine/filename->media-storage-uri)
(def filename->media-thumbnail-uri      engine/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   engine/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath engine/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       engine/filename->temporary-filepath)
(def file-props->filepath               engine/file-props->filepath)

; x.server-media.temporary-handler
(def download-temporary-file temporary-handler/download-temporary-file)

; x.server-media.thumbnail-handler
(def download-thumbnail  thumbnail-handler/download-thumbnail)
(def generate-thumbnail! thumbnail-handler/generate-thumbnail!)
