
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.engine
    (:require [mid-fruits.keyword              :as keyword]
              [mid-plugins.item-browser.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.engine
(def browser-uri             engine/browser-uri)
(def request-id              engine/request-id)
(def mutation-name           engine/mutation-name)
(def resolver-id             engine/resolver-id)
(def collection-name         engine/collection-name)
(def transfer-id             engine/transfer-id)
(def route-id                engine/route-id)
(def extended-route-id       engine/extended-route-id)
(def route-template          engine/route-template)
(def extended-route-template engine/extended-route-template)
(def component-id            engine/component-id)
(def dialog-id               engine/dialog-id)
(def load-extension-event    engine/load-extension-event)



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
  ;                                   {my-extension.my-type-browser/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [extension-id item-namespace server-response]
  (let [item-id-key   (keyword/add-namespace      item-namespace :id)
        mutation-name (mutation-name extension-id item-namespace :duplicate)]
       (get-in server-response [(symbol mutation-name) item-id-key])))
