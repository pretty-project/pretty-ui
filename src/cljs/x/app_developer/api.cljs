
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v0.2.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.api
    (:require [x.app-developer.cheat-codes]
              [x.app-developer.developer-tools]
              [x.app-developer.engine]
              [x.app-developer.request-browser]
              [x.app-developer.source-reader]
              [x.app-developer.welcome]
              [x.app-developer.database-browser :as database-browser]
              [x.app-developer.database-screen  :as database-screen]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-developer.database-browser
(def database-browser database-browser/view)
(def database-screen  database-screen/view)
