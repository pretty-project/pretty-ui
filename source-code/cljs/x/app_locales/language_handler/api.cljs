
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.2.0
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.language-handler.api
    (:require [x.app-locales.language-handler.subs :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-locales.language-handler.subs
(def get-app-languages     subs/get-app-languages)
(def app-multilingual?     subs/app-multilingual?)
(def get-selected-language subs/get-selected-language)
