
(ns pathom.query
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co]
              [com.wsscode.pathom3.interface.eql     :as pathom.eql]
              [pathom.env                            :as env]
              [mid-fruits.candy                      :refer [param return]]
              [mid-fruits.reader                     :as reader]
              [pathom.register                       :as register]
              [server-fruits.http                    :as http]))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->query
  ; @param (map) request
  ;  {:params (map)
  ;    {:query (vector)(opt)}}
  ;
  ; @usage
  ;  (pathom/request->query {...})
  ;
  ; @return (*)
  [request]
  ; BUG#4509
  ; A transit-params térkép helyett params térképből kiolvasott query hibás, abban az esetben
  ; ha a query értéke egy darab kulcsszó egy vektorban.
  ; Pl.: [:my-resolver]
  ;      =>
  ;      {:transit-params {:query [:my-resolver]}
  ;       :params         {:query :my-resolver}}
  (let [query (http/request->transit-param request :query)]
       ; Fájlfeltöltéskor a request törzse egy FormData objektum, amiből string típusként
       ; olvasható ki a query értéke ...
       (cond (string? query) (reader/string->mixed query)
             (vector? query) (return               query))))



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
