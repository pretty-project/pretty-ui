
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-installer.api
    (:require [x.server-installer.db-installer]
              [x.server-installer.media-installer]
              [x.server-installer.user-installer]
              [x.server-installer.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-installer.engine
(def server-installed?     engine/server-installed?)
(def get-installed-at      engine/get-installed-at)
(def get-installed-version engine/get-installed-version)
