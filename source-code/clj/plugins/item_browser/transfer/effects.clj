
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.transfer.effects
    (:require [mid-fruits.candy                      :refer [return]]
              [plugins.item-browser.transfer.helpers :as transfer.helpers]
              [x.server-core.api                     :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reg-transfer-browser-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  (fn [_ [_ extension-id item-namespace browser-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return browser-props))
                                 :target-path [:plugins :item-lister/transfer-items extension-id]}]}))
