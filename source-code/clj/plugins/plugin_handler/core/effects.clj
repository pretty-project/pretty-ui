
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.core.effects
    (:require [x.server-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :plugin-handler/init-plugin!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) plugin-props
  (fn [_ [_ plugin-id {:keys [base-route extended-route] :as plugin-props}]]
      {:dispatch-n [[:plugin-handler/reg-transfer-plugin-props! plugin-id plugin-props]
                    (if base-route     [:view-selector/add-base-route!     plugin-id plugin-props])
                    (if extended-route [:view-selector/add-extended-route! plugin-id plugin-props])]}))
