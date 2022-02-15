
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.1.8
; Compatibility: x4.3.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.api
    (:require [x.app-dictionary.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app.dictionary.engine
(def get-term     engine/get-term)
(def term-exists? engine/term-exists?)
(def look-up      engine/look-up)
(def looked-up    engine/looked-up)
(def add-term!    engine/add-term!)
(def add-terms!   engine/add-terms!)
