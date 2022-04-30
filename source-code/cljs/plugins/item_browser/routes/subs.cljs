
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.subs
    (:require [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def route-handled?      routes.subs/route-handled?)
(def get-extended-route  routes.subs/get-extended-route)
(def get-derived-item-id routes.subs/get-derived-item-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) browser-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r item-browser/get-item-route db :my-browser "my-item")
  ;  =>
  ;  "/@app-home/my-browser/my-item"
  ;
  ; @return (string)
  [db [_ browser-id item-id]]
  (r get-extended-route db browser-id item-id))
