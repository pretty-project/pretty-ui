
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-meta-item!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @return (map)
  [db [_ plugin-id item-key item-value]]
  (assoc-in db [:plugins :plugin-handler/meta-items item-key] item-value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items]))

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keywords in vector) keep-keys
  ;
  ; @return (map)
  [db [_ plugin-id keep-keys]]
  (update-in db [:plugins :plugin-handler/meta-items] select-keys keep-keys))
