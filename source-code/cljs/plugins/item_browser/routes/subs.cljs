
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.subs
    (:require [plugins.item-browser.transfer.subs :as transfer.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (string) item-id
  ;
  ; @usage
  ;  (r item-browser/get-item-route db :my-extension :item-namespace "my-item")
  ;
  ; @return (string)
  [db [_ extension-id item-namespace item-id]]
  (if-let [base-route (r transfer.subs/get-transfer-item db extension-id item-namespace :base-route)]
          (str base-route "/" item-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/get-item-route :my-extension :my-type "my-item"]
(a/reg-sub :item-browser/get-item-route get-item-route)