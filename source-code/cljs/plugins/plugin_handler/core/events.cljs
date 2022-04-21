
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
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id item-key] item-value))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/meta-items plugin-id]))

(defn reset-meta-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keywords in vector) keep-keys
  ;
  ; @return (map)
  [db [_ plugin-id keep-keys]]
  (update-in db [:plugins :plugin-handler/meta-items] select-keys keep-keys))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-item-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (string) item-id
  ;
  ; @return (map)
  [db [_ plugin-id item-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :item-id] item-id))

(defn set-view-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) view-id
  ;
  ; @return (map)
  [db [_ plugin-id view-id]]
  (assoc-in db [:plugins :plugin-handler/meta-items plugin-id :view-id] view-id))
