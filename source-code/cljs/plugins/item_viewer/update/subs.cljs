
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.update.subs
    (:require [mid-fruits.keyword                 :as keyword]
              [plugins.item-viewer.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name update.subs/get-mutation-name)



;; -- Delete item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-deleted-item-id :my-viewer {my-handler/delete-item! "my-item"})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ viewer-id server-response]]
  (let [mutation-name (r get-mutation-name db viewer-id :delete-item!)]
       (get server-response (symbol mutation-name))))



;; -- Duplicate item subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicated-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-duplicated-item-id :my-viewer {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ viewer-id server-response]]
  (let [mutation-name  (r get-mutation-name               db viewer-id :duplicate-item!)
        item-namespace (r transfer.subs/get-transfer-item db viewer-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (get-in server-response [(symbol mutation-name) id-key])))
