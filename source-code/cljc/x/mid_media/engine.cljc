
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-media.engine
    (:require [mid-fruits.io      :as io]
              [mid-fruits.string  :as string]
              [x.mid-media.config :as config]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name file-id
;  :0ce14671-e916-43ab-b057-0939329d4c1b
;
; @name filename
;  "0ce14671-ef916-43ab-b057-0939329d4c1b.png"
;
; @name thumbnail-filepath
;  "monoset-environment/media/thumbnails/0ce14671-e916-43ab-b057-0939329d4c1b.png"
;
; @name storage-filepath
;  "monoset-environment/media/storage/0ce14671-e916-43ab-b057-0939329d4c1b.png"



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn allowed-extensions
  ; @example
  ;  (media/allowed-extensions)
  ;  =>
  ;  ("mp4" "ttf" "gif" "webm" ...)
  ;
  ; @return (list)
  []
  (vals io/EXTENSIONS))

(defn filename->media-storage-uri
  ; @param (string) filename
  ;
  ; @example
  ;  (media/filename->media-storage-uri "ab3450.jpg")
  ;  =>
  ;  "/media/storage/ab3450.jpg"
  ;
  ; @return (string)
  [filename]
  (str config/STORAGE-URI-BASE filename))

(defn filename->media-thumbnail-uri
  ; @param (string) filename
  ;
  ; @example
  ;  (media/filename->media-thumbnail-uri "ab3450.jpg")
  ;  =>
  ;  "/media/thumbnails/ab3450.jpg"
  ;
  ; @return (string)
  [filename]
  (str config/THUMBNAIL-URI-BASE filename))

(defn filename->media-storage-filepath
  ; @param (string) filename
  ;
  ; @example
  ;  (media/filename->media-storage-filepath "ab3450.jpg")
  ;  =>
  ;  "monoset-environment/media/storage/ab3450.jpg"
  ;
  ; @return (string)
  [filename]
  (str config/STORAGE-FILEPATH-BASE filename))

(defn filename->media-thumbnail-filepath
  ; @param (string) filename
  ;
  ; @example
  ;  (media/filename->media-thumbnail-filepath "ab3450.jpg")
  ;  =>
  ;  "monoset-environment/media/thumbnails/ab3450.jpg"
  ;
  ; @return (string)
  [filename]
  (str config/THUMBNAIL-FILEPATH-BASE filename))

(defn filename->temporary-filepath
  ; @param (string) filename
  ;
  ; @example
  ;  (media/filename->temporary-filepath "ab3450.jpg")
  ;  =>
  ;  "monoset-environment/media/temp/ab3450.jpg"
  ;
  ; @return (string)
  [filename]
  (str config/TEMPORARY-FILEPATH-BASE filename))
