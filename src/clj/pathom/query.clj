
(ns pathom.query
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co]
              [com.wsscode.pathom3.interface.eql     :as pathom.eql]
              [pathom.env :as env]

              [mid-fruits.candy                      :refer [param]]
              [mid-fruits.reader                     :as reader]
              [pathom.register                       :as register]
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
  ;  {:multipart-params (map)
  ;    {:query (string)(opt)}
  ;   :params (map)
  ;    {:query (string)(opt)}}
  ;
  ; @usage
  ;  (pathom/request->query {...})
  ;
  ; @return (*)
  [request]
  (if-let [raw-query (http/request->param request :query)]
          (read-query raw-query)
          (if-let [raw-query (http/request->multipart-param request :query)]
                  (read-query raw-query))))



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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (map)
  [request]
  (let [query       (request->query request)
        environment (assoc @register/ENVIRONMENT :request request)]
       (process-query! environment query)))
