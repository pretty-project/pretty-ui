
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.8
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.engine
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id               engine/request-id)
(def data-item-path           engine/data-item-path)
(def meta-item-path           engine/meta-item-path)
(def mutation-name            engine/mutation-name)
(def resolver-id              engine/resolver-id)
(def collection-name          engine/collection-name)
(def new-item-uri             engine/new-item-uri)
(def add-new-item-event       engine/add-new-item-event)
(def route-id                 engine/route-id)
(def route-template           engine/route-template)
(def dialog-id                engine/dialog-id)
(def load-extension-event     engine/load-extension-event)
(def item-clicked-event       engine/item-clicked-event)
(def item-right-clicked-event engine/item-right-clicked-event)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-response->copy-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (engine/server-response->copy-id :my-extension :my-type
  ;                                   {my-extension.my-type-lister/duplicate-items! [{:my-type/id "my-item"}]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [extension-id item-namespace server-response]
  (let [item-id-key   (keyword/add-namespace      item-namespace :id)
        mutation-name (mutation-name extension-id item-namespace :duplicate)
        copy-items    (get server-response (symbol mutation-name))]
       (vector/->items copy-items item-id-key)))
