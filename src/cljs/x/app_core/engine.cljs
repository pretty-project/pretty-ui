
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.15
; Description:
; Version: v0.1.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.engine
    (:require [x.mid-core.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-core.engine
(def id            engine/id)
(def prot          engine/prot)
(def sub-prot      engine/sub-prot)
(def get-namespace engine/get-namespace)
