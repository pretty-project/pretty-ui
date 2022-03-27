
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.mount.effects
    (:require [plugins.item-browser.mount.events :as mount.events]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  (fn [{:keys [db]} [_ extension-id item-namespace body-props]]
      {:db (r mount.events/body-did-mount db extension-id item-namespace body-props)
       :dispatch [:item-browser/request-item! extension-id item-namespace]}))
