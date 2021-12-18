
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.20
; Description:
; Version: v0.2.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler
    (:require [x.mid-locales.name-handler :as name-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler
(def NAME-ORDERS        name-handler/NAME-ORDERS)
(def name->ordered-name name-handler/name->ordered-name)
