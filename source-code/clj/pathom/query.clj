
(ns pathom.query
    (:require [pathom.env         :as env]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.reader  :as reader]
              [pathom.register    :as register]
              [server-fruits.http :as http]
              [com.wsscode.pathom3.connect.operation :as pathom.co]
              [com.wsscode.pathom3.interface.eql     :as pathom.eql]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:params (map)(opt)
  ;    {:query (string or vector)(opt)}
  ;   :transit-params (map)(opt)
  ;    {:query (string or vector)(opt)}}
  ;
  ; @usage
  ;  (pathom/request->query {...})
  ;
  ; @return (vector)
  [request]
  ; Fájlfeltöltéskor a request törzse egy FormData objektum, amiből string típusként
  ; olvasható ki a query értéke ...
  (letfn [(f [query] (cond (vector? query) (return               query)
                           (string? query) (reader/string->mixed query)))]
         ; BUG#4509
         ; Abban az esetben ha a query értéke egy darab kulcsszó egy vektorban, akkor a transit-params
         ; térkép helyett params térképből kiolvasott query hibás lenne, ezért szükséges a query értékét
         ; a transit-params térképből kiolvasni!
         ; Pl.: [:my-resolver]
         ;      =>
         ;      {:transit-params {:query [:my-resolver]}
         ;       :params         {:query :my-resolver}}   <= ebben az esetben a query értéke nem vektor típus
         (if-let [query (http/request->transit-param request :query)]
                 (f query)
                 (if-let [query (http/request->param request :query)]
                         (f query)))))

(defn env->query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ;  {:request (map)
  ;    {:params (map)
  ;      {:query (vector)(opt)}}}
  ;
  ; @usage
  ;  (pathom/env->query {...})
  ;
  ; @return (vector)
  [env]
  (-> env env/env->request request->query))



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
  ; @return (map)
  [request]
  (let [query       (request->query request)
        environment (assoc @register/ENVIRONMENT :request request)]
       (process-query! environment query)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn debug-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [env _]
  (-> env env->query str println)
  (return "Follow the white rabbit"))

(defresolver debug
             ; WARNING! NON-PUBLIC! DO NOT USE!
             [env resolver-props]
             {:debug (debug-f env resolver-props)})

(register/reg-handler! :pathom/debug debug)
