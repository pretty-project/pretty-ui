
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-plugin-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ plugin-id item-key]]
  (get-in db [:plugins :plugin-handler/plugin-props plugin-id item-key]))
