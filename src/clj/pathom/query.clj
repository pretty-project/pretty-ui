
(ns pathom.query
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co]
              [com.wsscode.pathom3.interface.eql     :as pathom.eql]
              [mid-fruits.candy                      :refer [param]]
              [mid-fruits.reader                     :as reader]
              [server-fruits.http                    :as http]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  ;  (pathom/request->query {...})
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
  ;  (pathom/process-query! my-environment my-query)
  ;
  ; @return (map)
  [environment query]
  (pathom.eql/process environment query))
