
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.subs
    (:require [plugins.plugin-handler.download.subs :as download.subs]
              [re-frame.api                         :as r]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.subs
(def get-resolver-id download.subs/get-resolver-id)
(def data-received?  download.subs/data-received?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/data-received? :my-viewer]
(r/reg-sub :item-viewer/data-received? data-received?)
