
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.download-handler.routes
    (:require [io.api               :as io]
              [server-fruits.http   :as http]
              [x.media.core.helpers :as core.helpers]))



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
  (let [filename (http/request->path-param request :filename)
        filepath (core.helpers/filename->media-storage-filepath filename)]
       (if (io/file-exists? filepath)
           (http/media-wrap {:body      (io/file                filepath)
                             :mime-type (io/filepath->mime-type filepath)})
           (http/error-wrap {:error-message :file-not-found :status 404}))))
