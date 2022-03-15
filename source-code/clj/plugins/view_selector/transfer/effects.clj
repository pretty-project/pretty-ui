
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.effects
    (:require [mid-fruits.candy                       :refer [param return]]
              [plugins.view-selector.transfer.helpers :as transfer.helpers]
              [x.server-core.api                      :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/reg-transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  (fn [_ [_ extension-id selector-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id extension-id)
                                {:data-f      (fn [_] (return selector-props))}]}))
                                 ;:target-path [extension-id :view-selector/meta-items]}]}))
