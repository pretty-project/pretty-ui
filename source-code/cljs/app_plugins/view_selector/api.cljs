
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.2
; Compatibility: x4.5.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.api
    (:require [app-plugins.view-selector.engine]
              [app-plugins.view-selector.events :as events]
              [app-plugins.view-selector.subs   :as subs]
              [app-plugins.view-selector.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.view-selector.events
(def change-view! events/change-view!)

; app-plugins.view-selector.subs
(def get-selected-view-id subs/get-selected-view-id)
(def get-view-props       subs/get-view-props)

; app-plugins.view-selector.views
(def body   views/body)
(def header views/header)
(def view   views/view)
