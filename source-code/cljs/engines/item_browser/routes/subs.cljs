
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.routes.subs
    (:require [engines.engine-handler.routes.subs :as routes.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.routes.subs
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
  ;  (r get-item-route db :my-browser "my-item")
  ;  =>
  ;  "/@app-home/my-browser/my-item"
  ;
  ; @return (string)
  [db [_ browser-id item-id]]
  (r get-extended-route db browser-id item-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) browser-id
; @param (string) item-id
;
; @usage
;  [:item-browser/get-item-route db :my-browser "my-item"]
(r/reg-sub :item-browser/get-item-route get-item-route)
