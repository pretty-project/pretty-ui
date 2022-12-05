
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.routes.subs
    (:require [engines.engine-handler.routes.subs :as routes.subs]
              [engines.item-handler.transfer.subs :as transfer.subs]
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
  ; @param (keyword) handler-id
  ; @param (string) item-id
  ;
  ; @example
  ; (r get-item-route db :my-handler "my-item")
  ; =>
  ; "/@app-home/my-handler/my-item"
  ;
  ; @return (string)
  [db [_ handler-id item-id]]
  (let [extended-route (r get-extended-route db handler-id item-id)]
       (str extended-route)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) handler-id
; @param (string) item-id
;
; @usage
; [:item-handler/get-item-route db :my-handler "my-item"]
(r/reg-sub :item-handler/get-item-route get-item-route)
