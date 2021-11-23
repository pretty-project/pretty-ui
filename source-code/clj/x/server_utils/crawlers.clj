
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.01
; Description:
; Version: v0.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-utils.crawlers
    (:require [x.server-user.api :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crawler-rules
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A <meta name="robots"> tag content attribútumának értéke
  ;
  ; index:   Show this page in search results
  ; follow:  Follow the links on this page
  ; archive: Show a cached link in search results
  ; snippet: Show a text snippet or video preview in the search results for this page
  ;
  ; @param (map) request
  ; 
  ; @return (string)
  [request]
  (if (user/request->authenticated? request)
      "noindex, nofollow, noarchive, nosnippet"
      "index, follow, noarchive, max-snippet:200, max-image-preview:large"))
