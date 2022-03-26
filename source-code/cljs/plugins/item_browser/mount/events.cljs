
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.mount.events
    (:require [plugins.item-browser.core.events :as core.events]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace header-props]])

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:items-key (keyword)
  ;   :path-key (keyword)
  ;   :root-item-id (string)(opt)}
  ;
  ; @return (map)
  [db [_ extension-id item-namespace {:keys [items-key path-key root-item-id]}]]
  ; Az item-browser plugin minden Pathom lekérés küldésekor elküldi a szerver számára a body komponens
  ; {:items-key ...} és {:path-key ...} tulajdonságát.
  (cond-> db root-item-id (as-> % (r core.events/use-root-item-id! % extension-id item-namespace))
             :items-key   (assoc-in [:plugins :item-lister/meta-items extension-id :default-query-params :items-key] items-key)
             :path-key    (assoc-in [:plugins :item-lister/meta-items extension-id :default-query-params :path-key]  path-key)))

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]])

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]])
