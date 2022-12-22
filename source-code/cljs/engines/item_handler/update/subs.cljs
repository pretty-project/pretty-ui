
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-handler.update.subs
    (:require [engines.engine-handler.update.subs :as update.subs]
              [engines.item-handler.body.subs     :as body.subs]
              [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.transfer.subs :as transfer.subs]
              [keyword.api                        :as keyword]
              [map.api                            :as map]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.update.subs
(def get-mutation-name   update.subs/get-mutation-name)
(def get-mutation-answer update.subs/get-mutation-answer)



;; -- Save item subscriptions -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-saved-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-saved-item-id :my-handler {my-handler/save-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (let [new-item?      (r core.subs/new-item? db handler-id)
        action-key     (if new-item? :add-item! :save-item!)
        saved-item     (r get-mutation-answer db handler-id action-key server-response)
        item-namespace (r transfer.subs/get-transfer-item db handler-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key saved-item)))



;; -- Delete item subscriptions -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-deleted-item-id :my-handler {my-handler/delete-item! "my-item"})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (r update.subs/get-mutation-answer db handler-id :delete-item! server-response))



;; -- Duplicate item subscriptions --------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-copy-item-id :my-handler {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ handler-id server-response]]
  (let [copy-item      (r update.subs/get-mutation-answer db handler-id :duplicate-item! server-response)
        item-namespace (r transfer.subs/get-transfer-item db handler-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key copy-item)))



;; -- Undo delete item subscriptions ------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-recovered-item-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) server-response
  ;
  ; @example
  ; (r get-recovered-item-id :my-viewer {my-handler/undo-delete-item! {:my-type/id "my-item"}})
  ; =>
  ; "my-item"
  ;
  ; @return (string)
  [db [_ viewer-id server-response]]
  (let [recovered-item (r update.subs/get-mutation-answer db viewer-id :undo-delete-item! server-response)
        item-namespace (r transfer.subs/get-transfer-item db viewer-id :item-namespace)
        id-key         (keyword/add-namespace item-namespace :id)]
       (id-key recovered-item)))
