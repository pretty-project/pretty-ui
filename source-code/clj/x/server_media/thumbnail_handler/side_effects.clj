

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.side-effects
    (:require [server-fruits.image                     :as image]
              [server-fruits.io                        :as io]
              [x.server-media.core.helpers             :as core.helpers]
              [x.server-media.thumbnail-handler.config :as thumbnail-handler.config]))



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
  (let [filepath       (core.helpers/filename->media-storage-filepath   filename)
        thumbnail-path (core.helpers/filename->media-thumbnail-filepath filename)]
       (image/generate-thumbnail! filepath thumbnail-path {:max-size thumbnail-handler.config/DEFAULT-THUMBNAIL-SIZE})))

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
