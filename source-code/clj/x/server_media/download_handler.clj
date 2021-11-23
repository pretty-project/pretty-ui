
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.11
; Description:
; Version: v0.2.6
; Compatibility: x4.3.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.download-handler
    (:require [server-fruits.http    :as http]
              [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Fájl küldése a kliens eszközre
  ;
  ; @param (map) request
  ;   {:path-params (map)
  ;    {:filename (string)}}
  ;
  ; @return (map)
  [request]
  (let [filename (http/request->path-param                request :filename)
        filepath (engine/filename->media-storage-filepath filename)]
       (if (io/file-exists? filepath)
           (http/media-wrap {:body      (io/file                filepath)
                             :mime-type (io/filepath->mime-type filepath)})
           (http/error-wrap {:error-message :file-not-found :status 404}))))
