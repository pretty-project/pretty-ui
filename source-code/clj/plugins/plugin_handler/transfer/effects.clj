
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.transfer.effects
    (:require [mid-fruits.candy                       :refer [return]]
              [plugins.view-selector.transfer.helpers :as transfer.helpers]
              [x.server-core.api                      :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :plugin-handler/reg-transfer-plugin-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  (fn [_ [_ plugin-id plugin-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id plugin-id)
                                {:data-f      (fn [_] (return plugin-props))
                                 :target-path [:plugins :plugin-handler/transfer-items plugin-id]}]}))
