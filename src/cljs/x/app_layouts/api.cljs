
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.22
; Description:
; Version: v0.2.0
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.api
    (:require [x.app-layouts.headers  :as headers]
              [x.app-layouts.layout-a :as layout-a]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-layouts.headers
(def header-a headers/header-a)

; x.app-layouts.layout-a
(def layout-a layout-a/view)
