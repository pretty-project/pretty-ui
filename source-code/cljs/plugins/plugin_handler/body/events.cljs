
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.body.events
    (:require [mid-fruits.map :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (assoc-in db [:plugins :plugin-handler/body-props plugin-id] body-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (map)
  [db [_ plugin-id]]
  (dissoc-in db [:plugins :plugin-handler/body-props plugin-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-body-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ plugin-id body-props]]
  (update-in db [:plugins :plugin-handler/body-props plugin-id] merge body-props))
