
; WARNING! THIS IS AN OUTDATED VERSION OF A MONO-TEMPLATE FILE!



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.server-db.query-handler
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.map     :as map]
              [mid-fruits.reader  :as reader]
              [mid-fruits.vector  :as vector]
              [server-fruits.http :as http]
              [x.server-sync.api  :as sync]

              ; Pathom stuff
              [com.wsscode.pathom3.connect.indexes            :as pci]
              [com.wsscode.pathom3.interface.smart-map        :as psm]
              [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
              [com.wsscode.pathom.viz.ws-connector.core       :as pvc]
              [com.wsscode.pathom.viz.ws-connector.pathom3    :as p.connector]
              [com.wsscode.pathom3.interface.eql              :as p.eql]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def CONNECT_PARSER? true)

; @constant (vector)
(def SAMPLE-RESOLVERS
     [(pbir/constantly-resolver  :pi Math/PI)
      (pbir/single-attr-resolver :pi :tau #(* 2 %))])

; @constant (vector)
(def REGISTRY [SAMPLE-RESOLVERS])

; @constant (vector)
(def ENVIRONMENT
     (cond-> (pci/register REGISTRY)
             (param CONNECT_PARSER?)
             ; Give your environment a unique parser-id, this will ensure
             ; reconnects work as expected
             (p.connector/connect-env {::pvc/parser-id `env})))

(defn process!
  ; @param (map) request
  [request]
  (let [query       (sync/request->query request)
        environment (assoc ENVIRONMENT :request request)]
       (p.eql/process (param environment)
                      (param query))))
