
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.3.8
; Compatibility: x4.1.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.data-item-handler
    (:require [x.mid-db.data-item-handler :as data-item-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.data-item-handler
(def data-item->namespace   data-item-handler/data-item->namespace)
(def data-item->namespaced? data-item-handler/data-item->namespaced?)
(def data-item<-id          data-item-handler/data-item<-id)
