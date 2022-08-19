
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.routes
    (:require [server-fruits.http                      :as http]
              [server-fruits.io                        :as io]
              [x.server-media.core.helpers             :as core.helpers]
              [x.server-media.thumbnail-handler.config :as thumbnail-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [filename (http/request->path-param request :filename)
        filepath (core.helpers/filename->media-thumbnail-filepath filename)]
       (if (io/file-exists? filepath)
           (http/media-wrap {:body      (io/file                filepath)
                             :mime-type (io/filepath->mime-type filepath)})
           (let [filepath (core.helpers/filename->media-thumbnail-filepath thumbnail-handler.config/DEFAULT-THUMBNAIL-FILENAME)]
                (http/media-wrap {:body      (io/file                filepath)
                                  :mime-type (io/filepath->mime-type filepath)})))))
