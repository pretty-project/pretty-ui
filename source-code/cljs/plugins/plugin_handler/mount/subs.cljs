
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.mount.subs
    (:require [x.app-core.api :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-footer-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ plugin-id prop-key]]
  (get-in db [:plugins :plugin-handler/footer-props plugin-id prop-key]))

(defn get-body-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ plugin-id prop-key]]
  (get-in db [:plugins :plugin-handler/body-props plugin-id prop-key]))

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

(defn footer-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (some? (get-in db [:plugins :plugin-handler/footer-props plugin-id])))

(defn body-props-stored?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (some? (get-in db [:plugins :plugin-handler/body-props plugin-id])))

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

(defn footer-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (r footer-props-stored? db plugin-id))

(defn body-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (r body-props-stored? db plugin-id))

(defn header-did-mount?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ;
  ; @return (boolean)
  [db [_ plugin-id]]
  (r header-props-stored? db plugin-id))
