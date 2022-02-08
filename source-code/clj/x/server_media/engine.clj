
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.30
; Description:
; Version: v0.3.6
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.engine
    (:require [server-fruits.io   :as io]
              [x.mid-media.engine :as engine]))



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-storage-file!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-file! "my-file.png")
  [filename]
  (let [filepath (filename->media-storage-filepath filename)]
       (io/delete-file! filepath)))

(defn delete-storage-thumbnail!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-thumbnail! "my-file.png")
  [filename]
  (let [filepath (filename->media-thumbnail-filepath filename)]
       (io/delete-file! filepath)))

(defn duplicate-storage-file!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-file! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (filename->media-storage-filepath source-filename)
        copy-filepath   (filename->media-storage-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))

(defn duplicate-storage-thumbnail!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-thumbnail! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (filename->media-thumbnail-filepath source-filename)
        copy-filepath   (filename->media-thumbnail-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))
