
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.17
; Description:
; Version: v0.2.0
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.api
    (:require [x.server-dictionary.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-dictionary.engine
(def get-term     engine/get-term)
(def term-exists? engine/term-exists?)
(def look-up      engine/look-up)
(def looked-up    engine/looked-up)
(def add-term!    engine/add-term!)
(def add-terms!   engine/add-terms!)
