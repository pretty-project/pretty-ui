
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.mount.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ plugin-id footer-props]]
  (assoc-in db [:plugins :plugin-handler/footer-props plugin-id] footer-props))

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (assoc-in db [:plugins :plugin-handler/body-props plugin-id] body-props))

(defn store-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ plugin-id header-props]]
  (assoc-in db [:plugins :plugin-handler/header-props plugin-id] header-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/footer-props plugin-id]))

(defn remove-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/body-props plugin-id]))

(defn remove-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/header-props plugin-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-footer-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) footer-props
  ;
  ; @return (map)
  [db [_ plugin-id footer-props]]
  (update-in db [:plugins :plugin-handler/footer-props plugin-id] merge footer-props))

(defn update-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (update-in db [:plugins :plugin-handler/body-props plugin-id] merge body-props))

(defn update-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ plugin-id header-props]]
  (update-in db [:plugins :plugin-handler/header-props plugin-id] merge header-props))
