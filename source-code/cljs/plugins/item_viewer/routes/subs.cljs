
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.routes.subs
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
  ; @param (keyword) viewer-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r item-viewer/get-item-route db :my-viewer "my-item")
  ;  =>
  ;  "/@app-home/my-viewer/my-item"
  ;
  ; @return (string)
  [db [_ viewer-id item-id]]
  (r get-extended-route db viewer-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) viewer-id
; @param (string) item-id
;
; @usage
;  [:item-viewer/get-item-route db :my-viewer "my-item"]
(a/reg-sub :item-viewer/get-item-route get-item-route)
