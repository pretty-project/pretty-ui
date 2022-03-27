
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.subs
    (:require [plugins.item-browser.transfer.subs :as transfer.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def get-extended-route routes.subs/get-extended-route)



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



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-browser/get-item-route :my-browser "my-item"]
(a/reg-sub :item-browser/get-item-route get-item-route)
