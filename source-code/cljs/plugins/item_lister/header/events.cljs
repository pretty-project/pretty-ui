
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.header.events
    (:require [plugins.plugin-handler.header.events :as header.events]
              [x.app-core.api                       :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.header.events
(def store-header-props!  header.events/store-header-props!)
(def remove-header-props! header.events/remove-header-props!)
(def update-header-props! header.events/update-header-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ lister-id header-props]]
  (r store-header-props! db lister-id header-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  (r remove-header-props! db lister-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;
  ; @return (map)
  [db [_ lister-id header-props]]
  (r update-header-props! db lister-id header-props))
