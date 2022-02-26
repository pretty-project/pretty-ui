
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
  (-> db (assoc-in [extension-id :item-lister/meta-items] lister-props)
         ; XXX#8706
         ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
         ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
         (assoc-in [extension-id :item-lister/meta-items :item-namespace] item-namespace)))
