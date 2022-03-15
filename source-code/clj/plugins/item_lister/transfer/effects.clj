
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.transfer.effects
    (:require [mid-fruits.candy                     :refer [param return]]
              [plugins.item-lister.transfer.helpers :as transfer.helpers]
              [x.server-core.api                    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/reg-transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  (fn [_ [_ extension-id item-namespace lister-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id extension-id item-namespace)
                                {:data-f      (fn [_] (return lister-props))
                                 :target-path [extension-id :item-lister/transfer-items]}]}))
