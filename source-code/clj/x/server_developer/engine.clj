
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.14
; Description:
; Version: v0.2.6
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.engine
    (:require [mid-fruits.pretty        :as pretty]
              [x.server-core.api        :as a]
              [x.server-developer.debug :as debug]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-developer/dump-db!
  (fn [{:keys [db]} _]
      (println (pretty/mixed->string db))))
