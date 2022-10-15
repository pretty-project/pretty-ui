
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.download.subs
    (:require [plugins.plugin-handler.download.subs :as download.subs]
              [re-frame.api                         :as r]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.subs
(def get-resolver-id     download.subs/get-resolver-id)
(def get-resolver-answer download.subs/get-resolver-answer)
(def data-received?      download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
;
; @usage
;  [:file-editor/data-received? :my-editor]
(r/reg-sub :file-editor/data-received? data-received?)
