
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.subs
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

; @param (keyword) viewer-id
;
; @usage
;  [:item-viewer/data-received? :my-viewer]
(r/reg-sub :item-viewer/data-received? data-received?)
