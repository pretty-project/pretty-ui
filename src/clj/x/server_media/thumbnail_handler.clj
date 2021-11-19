
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.30
; Description:
; Version: v0.1.2
; Compatibility: x4.1.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler
    (:require [local-db.api          :as local-db]
              [server-fruits.http    :as http]
              [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def DEFAULT-THUMBNAIL-FILENAME "default.png")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-id->file-thumbnail-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [file-id])
  ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request])
  ; TODO ...
  ; Nem kizárólag képekhez készíthető thumbnail, de elsősorban most a képekhez
  ; lesz kidolgozva

(defn generate-image-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filepath
  [filepath])
;  (if (io/file-exists? filepath)
;      (let [input (as-file filepath)
;            image (javax.imageio.ImageIO/read input)])

(defn generate-pdf-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filepath
  [filepath])
  ; TODO ...

(defn generate-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) file-id
  [file-id]
  (let [filename  (local-db/get-document-item "files" file-id :filename)
        mime-type (io/filename->mime-type             filename)
        filepath  (engine/filename->media-storage-uri filename)]
       (case mime-type "image/bmp"       (generate-image-thumbnail! filepath)
                       "image/gif"       (generate-image-thumbnail! filepath)
                       "image/jpeg"      (generate-image-thumbnail! filepath)
                       "image/png"       (generate-image-thumbnail! filepath)
                       "image/webp"      (generate-image-thumbnail! filepath)
                       "application/pdf" (generate-pdf-thumbnail!   filepath))))
