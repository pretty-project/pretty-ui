
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.mount.events
    (:require [mid-fruits.map                      :refer [dissoc-in]]
              [plugins.plugin-handler.mount.events :as mount.events]
              [plugins.view-selector.core.events   :as core.events]
              [x.app-core.api                      :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.mount.events
(def store-body-props!  mount.events/store-body-props!)
(def remove-body-props! mount.events/remove-body-props!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ selector-id body-props]]
  (r store-body-props! db selector-id body-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (as-> db % (r remove-body-props!             % selector-id)
             (r core.events/remove-meta-items! % selector-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-did-mount body-did-mount)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :view-selector/body-will-unmount body-will-unmount)
