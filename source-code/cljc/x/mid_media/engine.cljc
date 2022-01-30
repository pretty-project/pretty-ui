
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.30
; Description:
; Version: v0.1.4
; Compatibility: x4.1.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-media.engine
    (:require [mid-fruits.io     :as io]
              [mid-fruits.string :as string]))



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



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def THUMBNAIL-URI-BASE      "/media/thumbnails/")

; @constant (string)
(def STORAGE-URI-BASE        "/media/storage/")

; @constant (string)
(def THUMBNAIL-FILEPATH-BASE "monoset-environment/media/thumbnails/")

; @constant (string)
(def STORAGE-FILEPATH-BASE   "monoset-environment/media/storage/")

; @constant (string)
(def TEMPORARY-FILEPATH-BASE "monoset-environment/media/temp/")

; @constant (integer)
(def MAXIMUM-DIRECTORY-ITEM-COUNT 256)

; @constant (px)
(def DEFAULT-THUMBNAIL-SIZE 128)



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
  (str STORAGE-URI-BASE filename))

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
  (str THUMBNAIL-URI-BASE filename))

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
  (str STORAGE-FILEPATH-BASE filename))

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
  (str THUMBNAIL-FILEPATH-BASE filename))

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
  (str TEMPORARY-FILEPATH-BASE filename))
