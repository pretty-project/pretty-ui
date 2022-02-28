
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.validators
    (:require [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [app-plugins.item-lister.engine :as engine]
              [app-plugins.item-lister.subs   :as subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]]
  (let [resolver-id    (engine/resolver-id extension-id item-namespace :get)
        document-count (get-in server-response [resolver-id :document-count])
        documents      (get-in server-response [resolver-id :documents])]
       (and (integer? document-count)
            (vector?  documents))))

(defn delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace server-response]]
  (let [mutation-name       (engine/mutation-name              extension-id item-namespace :delete)
        selected-item-count (r subs/get-selected-item-count db extension-id item-namespace)
        deleted-item-ids    (get server-response (symbol mutation-name))]
       (vector/count? deleted-item-ids selected-item-count)))

(defn undo-delete-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])

(defn duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])

(defn undo-duplicate-items-response-valid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [_ [_ extension-id item-namespace server-response]])
