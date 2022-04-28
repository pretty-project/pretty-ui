
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.routes.events
    (:require [plugins.plugin-handler.core.events :as core.events]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [x.app-core.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-derived-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (let [derived-item-id (r routes.subs/get-derived-item-id db plugin-id)]
       (r core.events/set-item-id! db plugin-id derived-item-id)))

(defn store-derived-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (let [derived-view-id (r routes.subs/get-derived-view-id db plugin-id)]
       (r core.events/set-view-id! db plugin-id derived-view-id)))
