
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.file-handler.side-effects
    (:require [io.api                      :as io]
              [x.server-media.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-storage-file!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-file! "my-file.png")
  [filename]
  (let [filepath (core.helpers/filename->media-storage-filepath filename)]
       (io/delete-file! filepath)))

(defn delete-storage-thumbnail!
  ; @param (string) filename
  ;
  ; @usage
  ;  (media/delete-storage-thumbnail! "my-file.png")
  [filename]
  (let [filepath (core.helpers/filename->media-thumbnail-filepath filename)]
       (io/delete-file! filepath)))

(defn duplicate-storage-file!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-file! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (core.helpers/filename->media-storage-filepath source-filename)
        copy-filepath   (core.helpers/filename->media-storage-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))

(defn duplicate-storage-thumbnail!
  ; @param (string) source-filename
  ; @param (string) copy-filename
  ;
  ; @usage
  ;  (media/duplicate-storage-thumbnail! "my-file.png" "my-copy.png")
  [source-filename copy-filename]
  (let [source-filepath (core.helpers/filename->media-thumbnail-filepath source-filename)
        copy-filepath   (core.helpers/filename->media-thumbnail-filepath copy-filename)]
       (io/copy-file! source-filepath copy-filepath)))
