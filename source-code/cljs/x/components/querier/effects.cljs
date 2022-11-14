
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.querier.effects
    (:require [x.components.querier.events :as querier.events]
              [re-frame.api                :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.compontents.querier/send-query!
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  (fn [_ [_ querier-id {:keys [query] :as querier-props}]]
      [:pathom/send-query! querier-id
                           {:query      query
                            :on-success [:x.compontents.querier/receive-query! querier-id querier-props]}]))

(r/reg-event-fx :x.compontents.querier/receive-query!
  ; @param (keyword) querier-id
  ; @param (map) querier-props
  ; @param (map) server-response
  (fn [_ [_ querier-id querier-props server-response]]
      {:db (r querier.events/receive-query! db querier-id querier-props server-response)}))
