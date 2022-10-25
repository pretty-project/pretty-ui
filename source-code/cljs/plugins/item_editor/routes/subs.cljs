
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.routes.subs
    (:require [plugins.item-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def route-handled?      routes.subs/route-handled?)
(def get-extended-route  routes.subs/get-extended-route)
(def get-derived-item-id routes.subs/get-derived-item-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-route
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r get-item-route db :my-editor "my-item")
  ;  =>
  ;  "/@app-home/my-editor/my-item"
  ;
  ; @return (string)
  [db [_ editor-id item-id]]
  (let [extended-route (r get-extended-route db editor-id item-id)]
       (str extended-route)))

(defn get-edit-route
  ; @param (keyword) editor-id
  ; @param (string) item-id
  ;
  ; @example
  ;  (r get-edit-route db :my-editor "my-item")
  ;  =>
  ;  "/@app-home/my-editor/my-item/edit"
  ;
  ; @return (string)
  [db [_ editor-id item-id]]
  (let [extended-route (r get-extended-route db editor-id item-id)]
       (str extended-route "/edit")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) editor-id
; @param (string) item-id
;
; @usage
;  [:item-editor/get-item-route db :my-editor "my-item"]
(r/reg-sub :item-editor/get-item-route get-item-route)

; @param (keyword) editor-id
; @param (string) item-id
;
; @usage
;  [:item-editor/get-edit-route db :my-editor "my-item"]
(r/reg-sub :item-editor/get-edit-route get-edit-route)
