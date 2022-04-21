
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.footer.subs
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
