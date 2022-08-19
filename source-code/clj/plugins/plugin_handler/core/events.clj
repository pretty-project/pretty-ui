
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-plugin-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  ;
  ; @return (map)
  [db [_ plugin-id plugin-props]]
  (assoc-in db [:plugins :plugin-handler/plugin-props plugin-id] plugin-props))
