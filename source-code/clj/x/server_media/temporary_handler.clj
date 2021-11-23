
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.30
; Description:
; Version: v0.3.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.temporary-handler
    (:require [server-fruits.http    :as http]
              [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-temporary-file
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;   {:path-params (map)
  ;    {:filename (string)}}
  ;
  ; @return (map)
  [request]
  (let [temporary-filename (http/request->path-param request :filename)
        temporary-filepath (engine/filename->temporary-filepath temporary-filename)]
    (if (io/file-exists? temporary-filepath)
        (http/media-wrap {:body      (io/file temporary-filepath)
                          :mime-type (io/filename->mime-type temporary-filename)})
        (http/error-wrap {:error-message :file-not-found :status 404}))))
