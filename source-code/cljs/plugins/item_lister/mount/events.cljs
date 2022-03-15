
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-lister.core.events     :as core.events]
              [plugins.item-lister.download.events :as download.events]
              [x.app-core.api                      :refer [r]]))



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
  [db [_ extension-id item-namespace header-props]]
  (assoc-in db [extension-id :item-lister/header-props] header-props))

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (as-> db % (assoc-in % [extension-id :item-lister/body-props] body-props)
             (r core.events/set-default-order-by! % extension-id item-namespace)))

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [extension-id :item-lister/header-props]))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  ; Az item-lister plugin elhagyásakor visszaállítja a plugin állapotát, így a következő
  ; betöltéskor nem villan fel a legutóbbi állapot!
  (as-> db % (r core.events/reset-meta-items!    % extension-id item-namespace)
             (r download.events/reset-downloads! % extension-id item-namespace)
             (dissoc-in % [extension-id :item-lister/body-props])))
