
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
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



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
  (r update.subs/get-mutation-answer db viewer-id :delete-item! server-response))



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
  (let [duplicated-item (r update.subs/get-mutation-answer db viewer-id :duplicate-item! server-response)
        item-namespace  (r transfer.subs/get-transfer-item db viewer-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key duplicated-item)))
