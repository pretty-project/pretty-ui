
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.subs
    (:require [mid-fruits.vector             :as vector]
              [plugins.item-editor.core.subs :as core.subs]
              [x.app-core.api                :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r download.subs/get-resolver-id db :my-extension :my-type :get)
  ;  =>
  ;  :my-handler/get-item
  ;
  ; @return (keyword)
  [db [_ extension-id item-namespace action-key]]
  (let [handler-key (r core.subs/get-meta-item db extension-id item-namespace :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key) "-item"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-suggestions?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [suggestion-keys (r core.subs/get-meta-item db extension-id item-namespace :suggestion-keys)]
       (vector/nonempty? suggestion-keys)))

(defn download-item?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [new-item?      (r core.subs/new-item?     db extension-id item-namespace)
        recovery-mode? (r core.subs/get-meta-item db extension-id item-namespace :recovery-mode?)]
       (not (or new-item? recovery-mode?))))

(defn download-data?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (or (r download-suggestions? db extension-id item-namespace)
      (r download-item?        db extension-id item-namespace)))
