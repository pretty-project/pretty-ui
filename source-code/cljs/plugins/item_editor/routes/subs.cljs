
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.subs
    (:require [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-editor/get-item-route db :my-extension :item-namespace "my-item")
  ;
  ; @return (string)
  [db [_ extension-id item-namespace item-id]]
  (if-let [base-route (r transfer.subs/get-transfer-item db extension-id item-namespace :base-route)]
          (str base-route "/" item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (string) item-id
;
; @usage
;  [:item-editor/get-item-route :my-extension :my-type "my-item"]
(a/reg-sub :item-editor/get-item-route get-item-route)
