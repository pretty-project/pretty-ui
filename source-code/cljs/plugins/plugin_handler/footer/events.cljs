
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.footer.events
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