
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.transfer.effects
    (:require [plugins.plugin-handler.transfer.effects]
              [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :view-selector/reg-transfer-selector-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [_ [_ selector-id selector-props]]
      [:plugin-handler/reg-transfer-plugin-props! selector-id selector-props]))
