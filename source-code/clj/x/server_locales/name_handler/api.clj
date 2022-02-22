
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.3.2
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler.api
    (:require [x.server-locales.name-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler.engine
(def NAME-ORDERS         engine/NAME-ORDERS)
(def name->ordered-name  engine/name->ordered-name)
(def request->name-order engine/request->name-order)
