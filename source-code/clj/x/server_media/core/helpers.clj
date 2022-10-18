
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.core.helpers
    (:require [x.mid-media.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-media.core.helpers
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
