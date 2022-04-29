
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.header.subs
    (:require [x.app-core.api :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ plugin-id prop-key]]
  (get-in db [:plugins :plugin-handler/header-props plugin-id prop-key]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (some? (get-in db [:plugins :plugin-handler/header-props plugin-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (r header-props-stored? db plugin-id))