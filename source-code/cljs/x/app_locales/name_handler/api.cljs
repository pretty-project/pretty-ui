
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler.api
    (:require [x.app-locales.name-handler.engine :as engine]
              [x.app-locales.name-handler.subs   :as subs]
              [x.app-locales.name-handler.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-locales.name-handler.engine
(def NAME-ORDERS        engine/NAME-ORDERS)
(def name->ordered-name engine/name->ordered-name)

; x.app-locales.name-handler.subs
(def get-name-order   subs/get-name-order)
(def get-ordered-name subs/get-ordered-name)

; x.app-locales.name-handler.views
(def name-order views/name-order)
