
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.item-lister.core.events     :as core.events]
              [plugins.item-lister.download.events :as download.events]
              [x.app-core.api                      :as a :refer [r]]))



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
  (assoc-in db [:plugins :item-lister/header-props extension-id] header-props))

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace body-props]]
  (as-> db % (assoc-in % [:plugins :item-lister/body-props extension-id] body-props)
             (r core.events/set-default-order-by! % extension-id item-namespace)))

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [:plugins :item-lister/header-props extension-id]))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (as-> db % (r core.events/reset-meta-items!    % extension-id item-namespace)
             (r download.events/reset-downloads! % extension-id item-namespace)
             (dissoc-in % [:plugins :item-lister/body-props extension-id])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/header-did-mount header-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/body-did-mount body-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/header-will-unmount header-will-unmount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :item-lister/body-will-unmount body-will-unmount)
