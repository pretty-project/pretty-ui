
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (assoc-in db [:plugins :item-lister/lister-props extension-id] lister-props))
