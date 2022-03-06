
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.file-handler.side-effects
    (:require [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-storage-file!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-file! "my-file.png")
  [filename]
  (let [filepath (engine/filename->media-storage-filepath filename)]
       (io/delete-file! filepath)))

(defn delete-storage-thumbnail!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-thumbnail! "my-file.png")
  [filename]
  (let [filepath (engine/filename->media-thumbnail-filepath filename)]
       (io/delete-file! filepath)))

(defn duplicate-storage-file!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-file! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (engine/filename->media-storage-filepath source-filename)
        copy-filepath   (engine/filename->media-storage-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))

(defn duplicate-storage-thumbnail!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-thumbnail! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (engine/filename->media-thumbnail-filepath source-filename)
        copy-filepath   (engine/filename->media-thumbnail-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))
