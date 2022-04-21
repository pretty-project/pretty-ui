
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.subs
    (:require [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def get-extended-route  routes.subs/get-extended-route)
(def get-derived-item-id routes.subs/get-derived-item-id)
(def get-derived-view-id routes.subs/get-derived-view-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r item-editor/get-item-route db :my-editor "my-item")
  ;  =>
  ;  "/@app-home/my-editor/my-item"
  ;
  ; @return (string)
  [db [_ editor-id item-id]]
  (r get-extended-route db editor-id item-id))

(defn get-view-route
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (r item-editor/get-view-route db :my-editor "my-item" :my-view)
  ;  =>
  ;  "/@app-home/my-editor/my-item/my-view"
  ;
  ; @return (string)
  [db [_ editor-id item-id view-id]]
  (let [item-route (r get-item-route db editor-id item-id)]
       (str item-route"/" (name view-id))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (string) item-id
;
; @usage
;  [:item-editor/get-item-route :my-editor "my-item"]
(a/reg-sub :item-editor/get-item-route get-item-route)
