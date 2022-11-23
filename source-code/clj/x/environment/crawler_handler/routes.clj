
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.crawler-handler.routes
    (:require [http.api                              :as http]
              [x.environment.crawler-handler.helpers :as crawler-handler.helpers]))



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
  (if-let [sitemap-xml (crawler-handler.helpers/sitemap-xml)]
          (http/xml-wrap   {:body sitemap-xml})
          (http/error-wrap {:error-message ":file-not-found" :status 404})))



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
  (if-let [robots-txt (crawler-handler.helpers/robots-txt request)]
          (http/text-wrap  {:body robots-txt})
          (http/error-wrap {:error-message ":file-not-found" :status 404})))
