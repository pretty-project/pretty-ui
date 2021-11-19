
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
    (:require [x.app-layouts.header-a :as header-a]
              [x.app-layouts.layout-a :as layout-a]
              [x.app-layouts.layout-b :as layout-b]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-layouts.headers
(def header-a header-a/header)

; x.app-layouts.layout-a
(def layout-a layout-a/layout)

; x.app-layouts.layout-b
(def layout-b layout-b/layout)
