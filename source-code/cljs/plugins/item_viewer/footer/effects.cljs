
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.footer.effects
    (:require [plugins.item-viewer.footer.events :as footer.events]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/footer-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) footer-props
  (fn [{:keys [db]} [_ viewer-id footer-props]]
      {:db (r footer.events/footer-did-mount db viewer-id footer-props)}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/footer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db (r footer.events/footer-will-unmount db viewer-id)}))
