
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.effects
    (:require [mid-fruits.candy                       :refer [return]]
              [plugins.view-selector.transfer.helpers :as transfer.helpers]
              [x.server-core.api                      :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/reg-transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [_ [_ selector-id selector-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id selector-id)
                                {:data-f      (fn [_] (return selector-props))
                                 :target-path [:plugins :view-selector/transfer-items selector-id]}]}))
