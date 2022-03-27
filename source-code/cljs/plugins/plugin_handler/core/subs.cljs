
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.subs
    (:require [plugins.plugin-handler.transfer.subs :as transfer.subs]
              [x.app-core.api                       :refer [r]]
              [x.app-sync.api                       :as sync]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-meta-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (*)
  [db [_ plugin-id item-key]]
  (get-in db [:plugins :plugin-handler/meta-items plugin-id item-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @example
  ;  (r get-request-id db :my-plugin)
  ;  =>
  ;  :my-handler/synchronize!
  ;
  ; @return (keyword)
  [db [_ plugin-id]]
  ; XXX#8519
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword (name handler-key) "synchronize!")))

(defn plugin-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  ; XXX#8519
  (let [request-id (r get-request-id db plugin-id)]
       (r sync/listening-to-request? db request-id)))
