
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.download.queries
    (:require [plugins.item-picker.core.subs     :as core.subs]
              [plugins.item-picker.download.subs :as download.subs]
              [x.app-core.api                    :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ picker-id]]
  (let [current-item-id (r core.subs/get-current-item-id db picker-id)]
       (r core.subs/use-query-params db picker-id {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (vector)
  [db [_ picker-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db picker-id :get-item)
        resolver-props (r get-request-item-resolver-props db picker-id)
        query          [`(~resolver-id ~resolver-props)]]
       (r core.subs/use-query-prop db picker-id query)))
