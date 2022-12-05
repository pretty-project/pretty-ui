
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.download.subs
    (:require [engines.engine-handler.download.subs :as download.subs]
              [re-frame.api                         :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.download.subs
(def get-resolver-id     download.subs/get-resolver-id)
(def get-resolver-answer download.subs/get-resolver-answer)
(def data-received?      download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
; [:file-editor/data-received? :my-editor]
(r/reg-sub :file-editor/data-received? data-received?)
