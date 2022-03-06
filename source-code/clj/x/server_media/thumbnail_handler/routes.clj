
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.routes
    (:require [server-fruits.http    :as http]
              [server-fruits.io      :as io]
              [x.server-media.engine :as engine]))



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
        filepath (engine/filename->media-thumbnail-filepath filename)]
       (if (io/file-exists? filepath)
           (http/media-wrap {:body      (io/file                filepath)
                             :mime-type (io/filepath->mime-type filepath)})
           (let [filepath (engine/filename->media-thumbnail-filepath DEFAULT-THUMBNAIL-FILENAME)]
                (http/media-wrap {:body      (io/file                filepath)
                                  :mime-type (io/filepath->mime-type filepath)})))))
