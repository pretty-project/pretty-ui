
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.16
; Description:
; Version: v0.1.4
; Compatibility: x4.2.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-sync.api
    (:require [x.mid-sync.query-handler :as query-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-sync.query-handler
(def append-to-query query-handler/append-to-query)
(def concat-queries  query-handler/concat-queries)
(def query-action    query-handler/query-action)
(def id->placeholder query-handler/id->placeholder)
