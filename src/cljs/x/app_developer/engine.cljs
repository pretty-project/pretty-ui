
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.2.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.engine
    (:require [x.app-core.api        :as a]
              [x.app-developer.debug :as debug]))



;; -- Side-effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  :x.app-developer/write-to-console!
  ; @param (*) content
  (fn [[content]]
      (debug/console content)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.test!
  [:x.app-ui/blow-bubble! {:content "It works!"}])
