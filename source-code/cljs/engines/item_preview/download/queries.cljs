
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-preview.download.queries
    (:require [engines.item-preview.core.subs     :as core.subs]
              [engines.item-preview.download.subs :as download.subs]
              [re-frame.api                       :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ preview-id]]
  (let [current-item-id (r core.subs/get-current-item-id db preview-id)]
       (r core.subs/use-query-params db preview-id {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) preview-id
  ;
  ; @return (vector)
  [db [_ preview-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db preview-id :get-item)
        resolver-props (r get-request-item-resolver-props db preview-id)
        query          [`(~resolver-id ~resolver-props)]]
       (r core.subs/use-query-prop db preview-id query)))
