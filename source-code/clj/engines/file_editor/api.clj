
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.api
    (:require [engines.file-editor.core.effects]
              [engines.file-editor.core.events]
              [engines.file-editor.transfer.effects]
              [engines.file-editor.core.subs :as core.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.file-editor.core.subs
(def get-editor-prop core.subs/get-editor-prop)
