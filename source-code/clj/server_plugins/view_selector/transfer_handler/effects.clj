
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.view-selector.transfer-handler.effects
    (:require [mid-fruits.candy                                     :refer [param return]]
              [server-plugins.view-selector.transfer-handler.engine :as transfer-handler.engine]
              [x.server-core.api                                    :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/reg-transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  (fn [_ [_ extension-id selector-props]]
      {:fx [:core/reg-transfer! (transfer-handler.engine/transfer-id extension-id)
                                {:data-f      (fn [_] (return selector-props))
                                 :target-path [extension-id :view-selector/meta-items]}]}))
