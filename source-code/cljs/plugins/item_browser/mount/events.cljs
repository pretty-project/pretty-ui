
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.mount.events
    (:require [plugins.item-browser.core.events :as core.events]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:items-key (keyword)
  ;   :path-key (keyword)
  ;   :root-item-id (string)(opt)}
  ;
  ; @return (map)
  [db [_ browser-id {:keys [items-key path-key root-item-id]}]]
  ; Az item-browser plugin minden Pathom lekérés küldésekor elküldi a szerver számára a body komponens
  ; {:items-key ...} és {:path-key ...} tulajdonságát.
  (cond-> db root-item-id (as-> % (r core.events/use-root-item-id! % browser-id))
             :items-key   (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :items-key] items-key)
             :path-key    (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :path-key]  path-key)))
