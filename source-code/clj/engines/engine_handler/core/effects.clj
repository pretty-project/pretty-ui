
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.core.effects
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :engine-handler/init-engine!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) engine-props
  (fn [_ [_ engine-id {:keys [base-route extended-route] :as engine-props}]]
      {:dispatch-n [[:engine-handler/reg-transfer-engine-props! engine-id engine-props]
                    (if base-route     [:view-selector/add-base-route!     engine-id engine-props])
                    (if extended-route [:view-selector/add-extended-route! engine-id engine-props])]}))