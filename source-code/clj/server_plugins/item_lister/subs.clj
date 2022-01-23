
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.01.22
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.subs
    (:require [mid-plugins.item-lister.subs :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.subs
(def get-lister-props subs/get-lister-props)
(def get-meta-item    subs/get-meta-item)
