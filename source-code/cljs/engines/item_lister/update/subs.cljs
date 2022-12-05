
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.item-lister.transfer.subs  :as transfer.subs]
              [keyword.api                        :as keyword]
              [re-frame.api                       :refer [r]]
              [vector.api                         :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.update.subs
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
  ; (r get-deleted-item-ids :my-lister {my-handler/delete-items! ["my-item"]})
  ; =>
  ; ["my-item"]
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
  ; (r get-duplicated-item-ids :my-lister {my-handler/duplicate-items! [{:my-type/id "my-item"}]})
  ; =>
  ; ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id server-response]]
  (let [item-namespace   (r transfer.subs/get-transfer-item db lister-id :item-namespace)
        duplicated-items (r get-mutation-answer             db lister-id :duplicate-items! server-response)
        id-key           (keyword/add-namespace item-namespace :id)]
       (vector/->items duplicated-items id-key)))
