
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.subs
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ extension-id prop-key]]
  (get-in db [extension-id :item-lister/header-props prop-key]))

(defn get-body-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ extension-id prop-key]]
  (get-in db [extension-id :item-lister/body-props prop-key]))

(defn header-mounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (some? (get-in db [extension-id :item-lister/header-props])))

(defn body-mounted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (some? (get-in db [extension-id :item-lister/body-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-header-prop get-header-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/get-body-prop get-body-prop)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/header-mounted? header-mounted?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-lister/body-mounted? body-mounted?)
