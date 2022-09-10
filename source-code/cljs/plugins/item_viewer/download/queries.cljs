
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.download.queries
    (:require [plugins.item-editor.core.subs           :as core.subs]
              [plugins.item-editor.download.subs       :as download.subs]
              [plugins.plugin-handler.download.queries :as download.queries]
              [x.app-core.api                          :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.download.queries
(def use-query-prop download.queries/use-query-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-item-resolver-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (map)
  ;  {:item-id (string)}
  [db [_ viewer-id]]
  (let [current-item-id (r core.subs/get-current-item-id db viewer-id)]
       (merge (r core.subs/get-query-params db viewer-id)
              {:item-id current-item-id})))

(defn get-request-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ;
  ; @return (vector)
  [db [_ viewer-id]]
  (let [resolver-id    (r download.subs/get-resolver-id   db viewer-id :get-item)
        resolver-props (r get-request-item-resolver-props db viewer-id)
        query          [`(~resolver-id ~resolver-props)]]
       (r use-query-prop db viewer-id query)))
