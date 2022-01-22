
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.30
; Description:
; Version: v0.4.2
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler
    (:require [local-db.api          :as local-db]
              [server-fruits.http    :as http]
              [server-fruits.image   :as image]
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

(defn generate-image-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  [filename]
  (let [filepath       (engine/filename->media-storage-filepath   filename)
        thumbnail-path (engine/filename->media-thumbnail-filepath filename)]
       (image/generate-thumbnail! filepath thumbnail-path {:max-size engine/DEFAULT-THUMBNAIL-SIZE})))

(defn generate-pdf-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  [filename])
  ; TODO ...

(defn generate-thumbnail!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  [filename]
  (let [mime-type (io/filename->mime-type filename)]
       (case mime-type "image/bmp"       (generate-image-thumbnail! filename)
                       "image/gif"       (generate-image-thumbnail! filename)
                       "image/jpeg"      (generate-image-thumbnail! filename)
                       "image/png"       (generate-image-thumbnail! filename)
                       "image/webp"      (generate-image-thumbnail! filename)
                       "application/pdf" (generate-pdf-thumbnail!   filename))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [filename (http/request->path-param                  request :filename)
        filepath (engine/filename->media-thumbnail-filepath filename)]
       (if (io/file-exists? filepath)
           (http/media-wrap {:body      (io/file                filepath)
                             :mime-type (io/filepath->mime-type filepath)})
           (let [filepath (engine/filename->media-thumbnail-filepath DEFAULT-THUMBNAIL-FILENAME)]
                (http/media-wrap {:body      (io/file                filepath)
                                  :mime-type (io/filepath->mime-type filepath)})))))
