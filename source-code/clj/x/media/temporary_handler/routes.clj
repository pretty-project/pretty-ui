
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.temporary-handler.routes
    (:require [http.api             :as http]
              [io.api               :as io]
              [x.media.core.helpers :as core.helpers]))



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
        temporary-filepath (core.helpers/filename->temporary-filepath temporary-filename)]
    (if (io/file-exists? temporary-filepath)
        (http/media-wrap {:body      (io/file temporary-filepath)
                          :mime-type (io/filename->mime-type temporary-filename)})
        (http/error-wrap {:error-message :file-not-found :status 404}))))