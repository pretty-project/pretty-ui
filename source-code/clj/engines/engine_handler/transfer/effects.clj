
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.transfer.effects
    (:require [engines.engine-handler.transfer.helpers :as transfer.helpers]
              [mid-fruits.candy                        :refer [return]]
              [re-frame.api                            :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :engine-handler/reg-transfer-engine-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) engine-props
  (fn [_ [_ engine-id engine-props]]
      {:fx [:core/reg-transfer! (transfer.helpers/transfer-id engine-id)
                                {:data-f      (fn [_] (return engine-props))
                                 :target-path [::engines :engine-handler/transfer-items engine-id]}]}))
