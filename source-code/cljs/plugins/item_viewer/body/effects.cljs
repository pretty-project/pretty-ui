
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.effects
    (:require [plugins.item-viewer.body.events :as body.events]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ viewer-id body-props]]
      {:db       (r body.events/body-did-mount db viewer-id body-props)
       :dispatch [:item-viewer/load-viewer! viewer-id]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db (r body.events/body-will-unmount db viewer-id)}))
