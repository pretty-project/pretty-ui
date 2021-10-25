
(ns pathom.register
    (:require [mid-fruits.vector :as vector]
              [x.server-core.api :as a :refer [r]]
              [x.server-db.api   :as db]
              [com.wsscode.pathom3.connect.indexes :as pathom.ci]))



;; -- Usage -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;
; (ns my-handlers
;     (:require [pathom.api         :as pathom :refer [ENVIRONMENT]]
;               [server-fruits.http :as http]))
;
; (defmutation         do-something! [env] ...)
; (pathom/reg-handler! do-something!)
;
; (defresolver get-anything [env] ...)
; (defmutation do-anything! [env] ...)
; (def HANDLERS [get-anything do-anything!])
; (pathom/reg-handlers! HANDLERS)

; @usage
;
; (defn process-query!
;   [request]
;   (let [environment (assoc @ENVIRONMENT :request request)]
;         query       (http/request->param request :query)
;         result (pathom/process-query! (param environment)
;                                       (pathom/read-query query)]
;        (http/map-wrap {:body {...}})))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (vector)
(def HANDLERS    (atom []))

; @atom (map)
(def ENVIRONMENT (atom {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-handler!
  ; @param (handler function) handler-f
  ;
  ; @usage
  ;  (pco/defmutation     do-something! [env] ...)
  ;  (pathom/reg-handler! do-something!)
  [handler-f]
  (swap! HANDLERS vector/conj-item handler-f))

(defn reg-handlers!
  ; @param (handler functions in vector) handler-fs
  ;
  ; @usage
  ;  (pco/defmutation     do-something! [env] ...)
  ;  (pco/defmutation     do-anything! [env] ...)
  ;  (def HANDLERS [do-something! do-anything!])
  ;  (pathom/reg-handlers! HANDLERS)
  [handler-fs]
  (swap! HANDLERS vector/concat-items handler-fs))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-environment!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (vector) registry
  ;
  ; @return (map)
  []
  (let [handlers    (deref HANDLERS)
        registry    [handlers]
        environment (pathom.ci/register registry)]
       (reset! ENVIRONMENT environment)))

(a/reg-handled-fx :pathom/reg-environment! reg-environment!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:pathom/reg-environment!]})
