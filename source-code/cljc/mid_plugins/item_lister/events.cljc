
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-lister.events)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace lister-props]]
  (assoc-in db [extension-id :item-lister/meta-items] lister-props))
