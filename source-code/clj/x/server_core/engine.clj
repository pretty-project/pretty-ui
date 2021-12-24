
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.01
; Description:
; Version: v1.1.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.engine
    (:require [server-fruits.http :as http]
              [x.mid-core.engine  :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.engine
(def dom-value     engine/dom-value)
(def id            engine/id)
(def get-namespace engine/get-namespace)
