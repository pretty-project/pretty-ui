
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.2.4
; Compatibility: x4.5.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.api
    (:require [app-plugins.item-browser.engine]
              [app-plugins.item-browser.events]
              [app-plugins.item-browser.subs  :as subs]
              [app-plugins.item-browser.views :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-browser.subs
(def get-current-item-id subs/get-current-item-id)

; app-plugins.item-browser.views
(def view views/view)
