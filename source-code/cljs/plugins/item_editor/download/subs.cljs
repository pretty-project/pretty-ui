
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.subs
    (:require [plugins.item-editor.mount.subs :as mount.subs]
              [x.app-core.api                 :refer [r]]))



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
  (let [handler-key (r mount.subs/get-body-prop db extension-id item-namespace :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key) "-item"))))
