
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.16
; Description:
; Version: v0.2.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-sync.query-handler
    (:require [com.wsscode.pathom3.connect.indexes   :as pathom.ci]
              [com.wsscode.pathom3.connect.operation :as pathom.co]
              [com.wsscode.pathom3.interface.eql     :as pathom.eql]
              [mid-fruits.candy                      :refer [param]]
              [mid-fruits.reader                     :as reader]
              [server-fruits.http                    :as http]
              [x.mid-sync.query-handler              :as query-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-sync.query-handler
(def append-to-query query-handler/append-to-query)
(def concat-queries  query-handler/concat-queries)
(def query-action    query-handler/query-action)
(def id->placeholder query-handler/id->placeholder)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn environment-register
  ; @param (vector) registry
  ;
  ; @return (map)
  [registry]
  (pathom.ci/register registry))

(defn read-query
  ; @param (string or vector) query
  ;
  ; @return (vector)
  [query]
  (reader/string->mixed query))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->query
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:query (string)}}
  ;
  ; @usage
  ;  (sync/request->query {...})
  ;
  ; @return (*)
  [request]
  (let [raw-query (http/request->transit-param request :query)]
       (read-query raw-query)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn process-query!
  ; @param (map) environment
  ; @param (*) query
  ;
  ; @usage
  ;  (sync/process-query! my-environment my-query)
  ;
  ; @return (map)
  [environment query]
  (pathom.eql/process (param environment)
                      (param query)))
