
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.header.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; -- Header-props events -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ plugin-id header-props]]
  (assoc-in db [:plugins :plugin-handler/header-props plugin-id] header-props))

(defn remove-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/header-props plugin-id]))

(defn update-header-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ plugin-id header-props]]
  (update-in db [:plugins :plugin-handler/header-props plugin-id] merge header-props))
