
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.side-effects
    (:require [server-fruits.image   :as image]
              [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) filename
  [filename])
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
