
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.download.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resolver-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) action-key
  ;
  ; @example
  ;  (r download.subs/get-resolver-id db :my-plugin :get-items)
  ;  =>
  ;  :my-handler/get-items
  ;
  ; @return (keyword)
  [db [_ plugin-id action-key]]
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword      (name handler-key)
                (str (name action-key)))))