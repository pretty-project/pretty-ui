
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.query.side-effects
    (:require [com.wsscode.pathom3.interface.eql :as pathom.eql]
              [pathom.query.helpers              :as query.helpers]
              [pathom.register.state             :as register.state]))



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

(defn process-request!
  ; @param (map) request
  ;
  ; @usage
  ;  (pathom/process-request! {...})
  ;
  ; @return (map)
  [request]
  (let [query       (query.helpers/request->query request)
        environment (assoc @register.state/ENVIRONMENT :request request)]
       (process-query! environment query)))
