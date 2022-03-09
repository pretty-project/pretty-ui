
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-editor.engine
    (:require [mid-fruits.keyword             :as keyword]
              [mid-plugins.item-editor.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-editor.engine
(def editor-uri         engine/editor-uri)
(def form-id            engine/form-id)
(def request-id         engine/request-id)
(def add-item-label     engine/add-item-label)
(def edit-item-label    engine/edit-item-label)
(def new-item-label     engine/new-item-label)
(def unnamed-item-label engine/unnamed-item-label)
(def mutation-name      engine/mutation-name)
(def resolver-id        engine/resolver-id)
(def collection-name    engine/collection-name)
(def transfer-id        engine/transfer-id)
(def route-id           engine/route-id)
(def route-template     engine/route-template)
(def base-route         engine/base-route)
(def component-id       engine/component-id)
(def dialog-id          engine/dialog-id)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (strings in vector)
(def COLORS ["var( --soft-blue )"
             "var( --soft-purple )"
             "var( --soft-green )"
             "var( --soft-red )"])



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
  ;                                   {my-extension.my-type-editor/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [extension-id item-namespace server-response]
  (let [item-id-key   (keyword/add-namespace      item-namespace :id)
        mutation-name (mutation-name extension-id item-namespace :duplicate)]
       (get-in server-response [(symbol mutation-name) item-id-key])))
