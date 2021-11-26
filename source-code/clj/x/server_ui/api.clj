
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.4.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.api
    (:require [x.server-ui.body   :as body]
              [x.server-ui.engine :as engine]
              [x.server-ui.head   :as head]
              [x.server-ui.html   :as html]
              [x.server-ui.shield :as shield]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-ui.body
(def body body/view)

; x.server-ui.engine
(def include-js      engine/include-js)
(def include-css     engine/include-css)
(def include-favicon engine/include-favicon)
(def include-font    engine/include-font)

; x.server-ui.head
(def head head/view)

; x.server-ui.html
(def html html/view)

; x.server-ui.shield
(def app-shield shield/view)
