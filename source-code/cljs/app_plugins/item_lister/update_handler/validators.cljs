
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.update-handler.validators
    (:require [app-plugins.item-lister.update-handler.subs :as update-handler.subs]
              [mid-fruits.vector                           :as vector]
              [x.app-core.api                              :refer [r]]))



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
  (let [mutation-name    (r update-handler.subs/get-mutation-name db extension-id item-namespace :delete)
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
  (let [mutation-name   (r update-handler.subs/get-mutation-name db extension-id item-namespace :undo-delete)
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
  (let [mutation-name    (r update-handler.subs/get-mutation-name db extension-id item-namespace :duplicate)
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
  (let [mutation-name    (r update-handler.subs/get-mutation-name db extension-id item-namespace :undo-duplicate)
        deleted-item-ids (get server-response (symbol mutation-name))]
       (vector/nonempty? deleted-item-ids)))
