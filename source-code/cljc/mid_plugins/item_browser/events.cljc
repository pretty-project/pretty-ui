
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-browser.events)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-browser-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (assoc-in db [extension-id :item-browser/meta-items] browser-props))
