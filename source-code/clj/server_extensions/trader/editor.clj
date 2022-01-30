
(ns server-extensions.trader.editor
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [server-extensions.trader.listener     :as listener]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def LISTENER-DOCUMENT-ID "61df813dd617ee732f3a0eff")



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- download-editor-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:source-code (string)}
  [env _]
  (let [listener-details (mongo-db/get-document-by-id "trader" LISTENER-DOCUMENT-ID)]
       {:source-code (get listener-details :trader/source-code)}))

(defresolver download-editor-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:trader/download-editor-data (map)
             ;    {:source-code (string)}}
             [env resolver-props]
             {:trader/download-editor-data (download-editor-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- test-source-code-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (?)
  [{:keys [request]} mutation-props]
  (let [source-code (get mutation-props :source-code)]
       (listener/run-source-code! source-code)))

(defmutation test-source-code!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'trader/test-source-code!}
             (test-source-code-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-source-code-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  ;  {:trader/source-code (string)
  ;   :trader/id (string)}
  [{:keys [request]} mutation-props]
  (let [source-code (get mutation-props :source-code)
        document    {:trader/source-code (param source-code)
                     :trader/id          LISTENER-DOCUMENT-ID}]
       (mongo-db/save-document! "trader" document
                                 {:prototype-f #(mongo-db/updated-document-prototype request :trader %)})))

(defmutation upload-source-code!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             ;  {:trader/source-code (string)
             ;   :trader/id (string)}}
             [env mutation-props]
             {::pathom.co/op-name 'trader/upload-source-code!}
             (upload-source-code-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-editor-data test-source-code! upload-source-code!])

(pathom/reg-handlers! ::handlers HANDLERS)
