
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.subs
    (:require [plugins.item-lister.transfer.subs  :as transfer.subs]
              [plugins.engine-handler.update.subs :as update.subs]
              [mid-fruits.keyword                 :as keyword]
              [mid-fruits.vector                  :as vector]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.engine-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; -- Delete items subscriptions ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r get-deleted-item-ids :my-lister {my-handler/delete-items! ["my-item"]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id server-response]]
  (r get-mutation-answer db lister-id :delete-items! server-response))



;; -- Duplicate items subscriptions -------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicated-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r get-duplicated-item-ids :my-lister {my-handler/duplicate-items! [{:my-type/id "my-item"}]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id server-response]]
  (let [item-namespace   (r transfer.subs/get-transfer-item db lister-id :item-namespace)
        duplicated-items (r get-mutation-answer             db lister-id :duplicate-items! server-response)
        id-key           (keyword/add-namespace item-namespace :id)]
       (vector/->items duplicated-items id-key)))
