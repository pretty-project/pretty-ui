
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.api
    (:require [x.app-ui.header.subs]
              [x.app-ui.header.events :as events]
              [x.app-ui.header.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-ui.header.events
(def set-header-title! events/set-header-title!)

; x.app-ui.header.views
(def view views/view)
