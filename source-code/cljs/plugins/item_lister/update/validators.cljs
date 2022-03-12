
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.update.validators
    (:require [mid-fruits.vector               :as vector]
              [plugins.item-lister.update.subs :as update.subs]
              [x.app-core.api                  :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db extension-id item-namespace :delete)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))

(defn undo-delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name   (r update.subs/get-mutation-name db extension-id item-namespace :undo-delete)
        recovered-items (get server-response (symbol mutation-name))]
       (vector/nonempty? recovered-items)))

(defn duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db extension-id item-namespace :duplicate)
        duplicated-items (get server-response (symbol mutation-name))]
       (vector/nonempty? duplicated-items)))

(defn undo-duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name    (r update.subs/get-mutation-name db extension-id item-namespace :undo-duplicate)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))
