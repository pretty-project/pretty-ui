

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.core.helpers
    (:require [x.mid-media.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-media.core.helpers
(def allowed-extensions                 core.helpers/allowed-extensions)
(def filename->media-storage-uri        core.helpers/filename->media-storage-uri)
(def filename->media-thumbnail-uri      core.helpers/filename->media-thumbnail-uri)
(def filename->media-storage-filepath   core.helpers/filename->media-storage-filepath)
(def filename->media-thumbnail-filepath core.helpers/filename->media-thumbnail-filepath)
(def filename->temporary-filepath       core.helpers/filename->temporary-filepath)
