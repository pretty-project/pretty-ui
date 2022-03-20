
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.update.subs
    (:require [mid-fruits.keyword                :as keyword]
              [plugins.item-editor.transfer.subs :as transfer.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mutation-name
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r update.subs/get-mutation-name db :my-extension :my-type :delete)
  ;  =>
  ;  "my-handler/delete-item!"
  ;
  ; @return (string)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db extension-id item-namespace :handler-key)]
       (str (name handler-key) "/"
            (name action-key)  "-item!")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-copy-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @example
  ;  (r update.subs/get-copy-id :my-extension :my-type {my-handler/duplicate-item! {:my-type/id "my-item"}})
  ;  =>
  ;  "my-item"
  ;
  ; @return (string)
  [db [_ extension-id item-namespace server-response]]
  (let [item-id-key   (keyword/add-namespace item-namespace :id)
        mutation-name (r get-mutation-name db extension-id item-namespace :duplicate)]
       (get-in server-response [(symbol mutation-name) item-id-key])))
