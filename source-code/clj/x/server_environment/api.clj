
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v0.2.6
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-environment.api
    (:require [x.server-environment.crawler-handler :as crawler-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-environment.crawler-handler
(def download-robots-txt  crawler-handler/download-robots-txt)
(def download-sitemap-xml crawler-handler/download-sitemap-xml)
(def crawler-rules        crawler-handler/crawler-rules)
