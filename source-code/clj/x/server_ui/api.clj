
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.4.2
; Compatibility: x4.2.6



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
(def body-props-prototype body/body-props-prototype)
(def body<-js-includes    body/body<-js-includes)

; x.server-ui.engine
(def include-favicon engine/include-favicon)
(def include-font    engine/include-font)

; x.server-ui.head
(def head head/view)

; x.server-ui.html
(def html html/view)

; x.server-ui.shield
(def app-shield shield/view)
