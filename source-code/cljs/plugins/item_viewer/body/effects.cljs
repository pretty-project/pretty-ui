
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.body.effects
    (:require [plugins.item-viewer.body.events :as body.events]
              [reagent.api                     :as reagent]
              [x.app-core.api                  :as a :refer [r]]))



;; -- Body lifecycles effects -------------------------------------------------
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

(a/reg-event-fx
  :item-viewer/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db (r body.events/body-will-unmount db viewer-id)}))

(a/reg-event-fx
  :item-viewer/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (?) %
  (fn [{:keys [db]} [_ viewer-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db viewer-id body-props)})))
