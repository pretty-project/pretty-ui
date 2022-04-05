
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.subs
    (:require [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def get-extended-route routes.subs/get-extended-route)



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (string) item-id
;
; @usage
;  [:item-editor/get-item-route :my-editor "my-item"]
(a/reg-sub :item-editor/get-item-route get-item-route)
