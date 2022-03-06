
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.crawler-handler.routes
    (:require [server-fruits.http                          :as http]
              [x.server-environment.crawler-handler.engine :as crawler-handler.engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-sitemap-xml
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:body (string)}
  [_]
  (if-let [sitemap-xml (crawler-handler.engine/sitemap-xml)]
          (http/xml-wrap   {:body sitemap-xml})
          (http/error-wrap {:error-message "File not found" :status 404})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-robots-txt
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  ;  {:body (string)}
  [request]
  (if-let [robots-txt (crawler-handler.engine/robots-txt request)]
          (http/text-wrap  {:body robots-txt})
          (http/error-wrap {:error-message "File not found" :status 404})))
