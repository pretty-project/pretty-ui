
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.querier.effects
    (:require [x.app-components.querier.events :as querier.events]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :components.querier/send-query!
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  (fn [_ [_ querier-id {:keys [query] :as querier-props}]]
      [:pathom/send-query! querier-id
                           {:query      query
                            :on-success [:components.querier/receive-query! querier-id querier-props]}]))

(a/reg-event-fx
  :components.querier/receive-query!
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ; @param (map) server-response
  (fn [_ [_ querier-id querier-props server-response]]
      {:db (r querier.events/receive-query! db querier-id querier-props server-response)}))
