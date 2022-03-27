
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
  ; - A pluginok a különböző lekérések elküldéséhez ugyanazt az azonosítót használják,
  ;   mert egy közös azonosítóval egyszerűbb megállapítani, hogy valamelyik lekérés folyamatban
  ;   van-e (a plugin-synchronizing? függvénynek elegendő egy request-id azonosítót figyelnie).
  ; - Ha szükséges, akkor a különböző lekéréseket el lehet látni egyedi azonosítókkal.
  (let [handler-key (r transfer.subs/get-transfer-item db plugin-id :handler-key)]
       (keyword (name handler-key) "synchronize!")))

(defn plugin-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (let [request-id (r get-request-id db plugin-id)]
       (r sync/listening-to-request? db request-id)))
