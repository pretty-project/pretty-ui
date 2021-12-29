
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.8.2
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.engine
    (:require [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri              engine/editor-uri)
(def form-id                 engine/form-id)
(def item-id->new-item?      engine/item-id->new-item?)
(def item-id->editor-label   engine/item-id->editor-label)
(def new-item-label          engine/new-item-label)
(def unnamed-item-label      engine/unnamed-item-label)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def resolver-id             engine/resolver-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def parent-uri              engine/parent-uri)
(def render-event            engine/render-event)
(def dialog-id               engine/dialog-id)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-response->copy-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (engine/server-response->copy-id :my-extension :my-type
  ;                                   {my-extension/duplicate-my-type-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [extension-id item-namespace server-response]
  (let [item-id-key   (keyword item-namespace "id")
        mutation-name (mutation-name extension-id item-namespace :duplicate)]
       (get-in server-response [(symbol mutation-name) item-id-key])))