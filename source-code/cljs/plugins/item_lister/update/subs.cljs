
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.subs
    (:require [plugins.item-lister.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.update.subs :as update.subs]
              [mid-fruits.keyword                 :as keyword]
              [mid-fruits.vector                  :as vector]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.update.subs
(def get-mutation-name update.subs/get-mutation-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-deleted-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/server-response->deleted-item-ids :my-lister {my-handler/delete-items! ["my-item"]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id server-response]]
  (let [mutation-name (r get-mutation-name db lister-id :delete-items!)]
       (get server-response (symbol mutation-name))))

(defn get-duplicated-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/server-response->duplicated-item-ids :my-lister {my-handler/duplicate-items! [{:my-type/id "my-item"}]})
  ;  =>
  ;  ["my-item"]
  ;
  ; @return (strings in vector)
  [db [_ lister-id server-response]]
  (let [mutation-name  (r get-mutation-name               db lister-id :duplicate-items!)
        item-namespace (r transfer.subs/get-transfer-item db lister-id :item-namespace)
        copy-items     (get server-response (symbol mutation-name))
        id-key         (keyword/add-namespace item-namespace :id)]
       (vector/->items copy-items id-key)))
