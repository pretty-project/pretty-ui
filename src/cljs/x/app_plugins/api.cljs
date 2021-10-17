
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.05.08
; Description:
; Version: v0.2.4
; Compatibility: x4.3.7



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-plugins.api
    (:require [x.app-plugins.editor     :as editor]
              [x.app-plugins.sortable   :as sortable]
              [x.app-plugins.sortable-2 :as sortable-2]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-plugins.editor
(def editor editor/view)

; x.app-plugins.sortable
(def sortable               sortable/view)
(def sortable-2             sortable-2/view)
(def get-sortable-prop      sortable-2/get-sortable-prop)
(def add-sortable-item!     sortable-2/add-sortable-item!)
(def add-sortable-items!    sortable-2/add-sortable-items!)
(def inject-sortable-item!  sortable-2/inject-sortable-item!)
(def remove-sortable-item!  sortable-2/remove-sortable-item!)
(def remove-sortable-items! sortable-2/remove-sortable-items!)
