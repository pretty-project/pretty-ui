
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.11
; Description:
; Version: v0.1.4
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-media.engine
    (:require [x.mid-media.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-media.engine
(def DEFAULT-THUMBNAIL-SIZE             engine/DEFAULT-THUMBNAIL-SIZE)
(def allowed-extensions                 engine/allowed-extensions)
(def filename->media-storage-uri        engine/filename->media-storage-uri)
(def filename->media-thumbnail-uri      engine/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   engine/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath engine/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       engine/filename->temporary-filepath)
(def file-props->filepath               engine/file-props->filepath)
